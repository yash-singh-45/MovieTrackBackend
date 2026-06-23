package com.cinetrackbackend.backend.controller;

import com.cinetrackbackend.backend.service.UserMovieService;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.cinetrackbackend.backend.dto.MovieDto;
import com.cinetrackbackend.backend.dto.UserMovieResponseDto;
import com.cinetrackbackend.backend.entity.Movie;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/list")
public class ListController {
    
    private final UserMovieService userMovieService;

    ListController(UserMovieService userMovieService) {
        this.userMovieService = userMovieService;
    }

    @GetMapping("/watchlist/get")
    public ResponseEntity<List<MovieDto>> getWatchList( Authentication authentication){
        String username = authentication.getName();

        return ResponseEntity.ok(userMovieService.getWatchList(username));
    }

    @GetMapping("/favourites/get")
    public ResponseEntity<List<MovieDto>> getFavourites( Authentication authentication){
        String username = authentication.getName();

        return ResponseEntity.ok(userMovieService.getFavourites(username));
    }

    @PostMapping("/watchlist/add")
    public ResponseEntity<String> addWatchList(@RequestBody  MovieDto requestMovie, Authentication authentication) {
        String username = authentication.getName();

        userMovieService.addToWatchlist(username, requestMovie);

        return ResponseEntity.ok().body("Movie Added to Watchlist");
    }

    @PostMapping("/favourites/add")
    public ResponseEntity<String> addFavourites(@RequestBody  MovieDto requestMovie, Authentication authentication) {
        String username = authentication.getName();

        userMovieService.addToFavourites(username, requestMovie);

        return ResponseEntity.ok().body("Movie Added to Favourites");
    }
    

    @PostMapping("/watchlist/remove")
    public ResponseEntity<Movie> removeFromWatchList(@RequestBody String imdbId, Authentication authentication){
        String username = authentication.getName();

        Movie movie = userMovieService.removeFromWatchList(imdbId, username);

        return ResponseEntity.ok().body(movie);
    }

    @PostMapping("/favourites/remove")
    public ResponseEntity<Movie> removeFromFavourites(@RequestBody String imdbId, Authentication authentication){
        String username = authentication.getName();

        Movie movie = userMovieService.removeFromFavourites(imdbId, username);

        return ResponseEntity.ok().body(movie);
    }
    
    @GetMapping("/check/{imdbId}")
    public ResponseEntity<UserMovieResponseDto> isInWatchList(@PathVariable String imdbId, Authentication authentication) {
        String username = authentication.getName();

        UserMovieResponseDto check = userMovieService.checkMovie(username, imdbId);

        return ResponseEntity.ok(check);
    }


    
}
