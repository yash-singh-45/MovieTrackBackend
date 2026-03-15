package com.cinetrackbackend.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinetrackbackend.backend.dto.LoginRequestDto;
import com.cinetrackbackend.backend.dto.LoginResponseDto;
import com.cinetrackbackend.backend.dto.SignUpRequestDto;
import com.cinetrackbackend.backend.dto.SignUpResponseDto;
import com.cinetrackbackend.backend.security.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> postMethodName(@Valid @RequestBody SignUpRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> postMethodName(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
    
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser (@RequestParam("token") String token) {
        authService.verifyEmail(token);
        
        return ResponseEntity.ok("Account verified successfully. You can now log in");
    }
    
}
