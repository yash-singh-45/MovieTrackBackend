package com.cinetrackbackend.backend.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cinetrackbackend.backend.dto.MovieDto;
import com.cinetrackbackend.backend.dto.UserMovieResponseDto;
import com.cinetrackbackend.backend.entity.Movie;
import com.cinetrackbackend.backend.entity.User;
import com.cinetrackbackend.backend.entity.UserMovie;
import com.cinetrackbackend.backend.repository.UserMovieRepository;
import com.cinetrackbackend.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserMovieService {

    private final UserMovieRepository userMovieRepository;
    private final MovieService movieService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void addToWatchlist(String username, MovieDto requestMovie) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Movie movie = movieService.getOrCreateMovie(requestMovie);

        UserMovie userMovie = userMovieRepository.findByUserAndMovie(user, movie).orElse(new UserMovie());

        userMovie.setWatchList(true);
        userMovie.setUser(user);
        userMovie.setMovie(movie);

        userMovieRepository.save(userMovie);
    }

    public void addToFavourites(String username, MovieDto requestMovie) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Movie movie = movieService.getOrCreateMovie(requestMovie);

        UserMovie userMovie = userMovieRepository.findByUserAndMovie(user, movie).orElse(new UserMovie());

        userMovie.setUser(user);
        userMovie.setMovie(movie);
        userMovie.setFavourite(true);

        userMovieRepository.save(userMovie);
    }

    public List<MovieDto> getWatchList(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!!"));

        List<UserMovie> list = userMovieRepository.findByUserAndWatchList(user, true);

        List<MovieDto> watchList = list.stream()
                .map((userMovie) -> modelMapper.map(userMovie.getMovie(), MovieDto.class))
                .toList();

        return watchList;
    }

    public List<MovieDto> getFavourites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!!"));

        List<UserMovie> list = userMovieRepository.findByUserAndFavourite(user, true);

        List<MovieDto> favourites = list.stream()
                .map((userMovie) -> modelMapper.map(userMovie.getMovie(), MovieDto.class))
                .toList();

        return favourites;
    }

    public UserMovieResponseDto checkMovie(String username, String imdbId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!!"));

        Optional<Movie> movie = movieService.getMovieByImdbId(imdbId);

        UserMovieResponseDto response = new UserMovieResponseDto();
        
        if (!movie.isPresent()) {
            response.setFavourite(false);
            response.setWatchlist(false);
            response.setImdbdId(imdbId);
            return response;
        }
        
        Optional<UserMovie> usermovie = userMovieRepository.findByUserAndMovie(user, movie.get());

        if (!usermovie.isPresent()) {
            response.setFavourite(false);
            response.setWatchlist(false);
            response.setImdbdId(imdbId);
            return response;
        }else{
            response.setFavourite(usermovie.get().isFavourite());
            response.setWatchlist(usermovie.get().isWatchList());
            response.setImdbdId(imdbId);

            return response;
        }
    }

    public Movie removeFromWatchList(String imdbId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!!"));

        Movie movie = movieService.getMovieByImdbId(imdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found!!"));

        UserMovie userMovie = userMovieRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new RuntimeException("Bad Request!!"));

        if (!userMovie.isWatchList()) {
            throw new RuntimeException("Bad Request!!");
        }

        userMovie.setWatchList(false);

        if (userMovie.isFavourite()) {
            userMovieRepository.save(userMovie);
        } else {
            userMovieRepository.delete(userMovie);
        }

        return movie;
    }

    public Movie removeFromFavourites(String imdbId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!!"));

        Movie movie = movieService.getMovieByImdbId(imdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found!!"));

        UserMovie userMovie = userMovieRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new RuntimeException("Bad Request!!"));

        if (!userMovie.isFavourite()) {
            throw new RuntimeException("Bad Request!!");
        }

        userMovie.setFavourite(false);

        if (userMovie.isWatchList()) {
            userMovieRepository.save(userMovie);
        } else {
            userMovieRepository.delete(userMovie);
        }

        return movie;
    }
}
