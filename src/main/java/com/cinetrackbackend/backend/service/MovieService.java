package com.cinetrackbackend.backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cinetrackbackend.backend.dto.MovieDto;
import com.cinetrackbackend.backend.entity.Movie;
import com.cinetrackbackend.backend.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public Movie getOrCreateMovie(MovieDto requestMovie){
        return movieRepository.findByImdbId(requestMovie.imdbId).orElseGet(()->{
            Movie movie = new Movie();
            movie.setImdbId(requestMovie.imdbId);
            movie.setPosterPath(requestMovie.posterPath);
            movie.setRating(requestMovie.rating);
            movie.setTitle(requestMovie.title);
            
            return movieRepository.save(movie);
        });
    }

    public Optional<Movie> getMovieByImdbId(String imdbId){
        return movieRepository.findByImdbId(imdbId);
    }

    public Movie saveMovie(Movie movie){
        return movieRepository.save(movie);
    }
}
