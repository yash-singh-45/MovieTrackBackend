package com.cinetrackbackend.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HealthCheckController {
    
    @GetMapping("/api/ping")
    public ResponseEntity<String> check(){
        return ResponseEntity.ok().body("Awake");
    }
}
