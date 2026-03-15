package com.cinetrackbackend.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinetrackbackend.backend.dto.UserDto;
import com.cinetrackbackend.backend.security.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getUserById(id));
    }

}
