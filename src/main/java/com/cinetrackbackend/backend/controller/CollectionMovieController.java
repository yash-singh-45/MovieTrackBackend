package com.cinetrackbackend.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cinetrackbackend.backend.dto.CollectionMovieRequestDto;
import com.cinetrackbackend.backend.dto.MovieDto;
import com.cinetrackbackend.backend.service.CollectionMovieService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/collectionmovie")
@RequiredArgsConstructor
public class CollectionMovieController {
    
    private final CollectionMovieService collectionMovieService;

    @PostMapping("/add")
    public ResponseEntity<String> addMovieToCollection(@RequestBody CollectionMovieRequestDto requestDto, Authentication authentication) {
        
        String username = authentication.getName();

        collectionMovieService.addMovieToCollection(requestDto, username);
        return ResponseEntity.ok("Movie added to collection");
    }

    @GetMapping("/get/{publicId}")
    public ResponseEntity<List<MovieDto>> getMethodName(@PathVariable String publicId, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : null;

        List<MovieDto> movies = collectionMovieService.getMoviesFromCollection(publicId, username);

        return ResponseEntity.ok(movies);
    }
}
