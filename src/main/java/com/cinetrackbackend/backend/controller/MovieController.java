package com.cinetrackbackend.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinetrackbackend.backend.dto.TmdbSimilarResponseDto;
import com.cinetrackbackend.backend.dto.TrailerResponse;
import com.cinetrackbackend.backend.service.TmdbService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final TmdbService tmdbService;

    @GetMapping("/trailer/{imdbId}/{type}")
    public ResponseEntity<TrailerResponse> getTrailerKey(@PathVariable String imdbId, @PathVariable String type) {
        String trailerUrl = tmdbService.getTrailerByImdbId(imdbId, type);

        if (trailerUrl == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new TrailerResponse(trailerUrl));
    }

    @GetMapping("/similar/{imdbId}/{type}")
    public ResponseEntity<TmdbSimilarResponseDto> getSimilarMovies(@PathVariable String imdbId, @PathVariable String type) {
        return tmdbService.getSimilarMovies(imdbId, type)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
