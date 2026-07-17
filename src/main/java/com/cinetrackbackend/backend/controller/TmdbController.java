package com.cinetrackbackend.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinetrackbackend.backend.service.TmdbService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tmdbapi")
@RequiredArgsConstructor
public class TmdbController {
    
    private final TmdbService tmdbService;

    @GetMapping("/persons/{name}")
    public ResponseEntity<String> getPersons(@PathVariable String name){
        return ResponseEntity.ok(tmdbService.searchPersons(name));
    }
    
    @GetMapping("/heromovies")
    public ResponseEntity<String> getHeroMovies(){
        return ResponseEntity.ok(tmdbService.getHeroMovies());
    }

    @GetMapping("/getimdb/{media_type}/{tmdbId}")
    public ResponseEntity<String> getImdbId(@PathVariable String media_type, @PathVariable String tmdbId){
        return ResponseEntity.ok(tmdbService.getImdbId(tmdbId, media_type));
    }
}
