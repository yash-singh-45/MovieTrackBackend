package com.cinetrackbackend.backend.security;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cinetrackbackend.backend.dto.LoginRequestDto;
import com.cinetrackbackend.backend.dto.LoginResponseDto;
import com.cinetrackbackend.backend.dto.SignUpRequestDto;
import com.cinetrackbackend.backend.dto.SignUpResponseDto;
import com.cinetrackbackend.backend.dto.UserDto;
import com.cinetrackbackend.backend.entity.User;
import com.cinetrackbackend.backend.entity.VerificationToken;
import com.cinetrackbackend.backend.repository.UserRepository;
import com.cinetrackbackend.backend.repository.VerificationRepository;
import com.cinetrackbackend.backend.service.impl.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final EmailService emailService;
    private final VerificationRepository verificationRepository;

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    public SignUpResponseDto register(SignUpRequestDto requestuser) {

        if(userRepository.existsByUsername(requestuser.getUsername())){
            throw new IllegalArgumentException("Username already taken");
        }

        User newUser = modelMapper.map(requestuser, User.class);
        newUser.setPassword(passwordEncoder.encode(requestuser.getPassword()));

        User savedUser = userRepository.save(newUser);

        String tokenStr = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(tokenStr);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(24));
        verificationRepository.save(verificationToken);

        emailService.sendVerificationEmail(savedUser.getEmail(), tokenStr);

        return SignUpResponseDto.builder()
                .email(savedUser.getEmail())
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .message("Registration successful. Please verify your email.")
                .build();
    }

    @Transactional
    public void verifyEmail(String tokenStr) {
        VerificationToken token = verificationRepository.findByToken(tokenStr).orElseThrow(
                () -> new IllegalArgumentException("Invalid Token"));

        if (token == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            verificationRepository.delete(token);
            throw new IllegalArgumentException("Token has expired. Please register again");
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        verificationRepository.deleteByToken(tokenStr);

    }

    public LoginResponseDto login(LoginRequestDto request) {

        System.out.println("Login Called");

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Invalid Credentials"));

        System.out.println("User found: " + user.getUsername());
        System.out.println("Enabled: " + user.isEnabled());
        System.out.println("Stored password hash: " + user.getPassword());
        System.out.println("Password matches: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        if (!user.isEnabled()) {
            throw new IllegalStateException("Email not verified. Please check your inbox");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = authUtil.generateAccessToken((User) authentication.getPrincipal());

        return new LoginResponseDto(token, user.getId());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
        .orElseThrow( ()-> new IllegalArgumentException("User not found with id "+id));

        return modelMapper.map(user, UserDto.class);
    }

}
