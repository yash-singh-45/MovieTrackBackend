package com.cinetrackbackend.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinetrackbackend.backend.entity.Movie;
import com.cinetrackbackend.backend.entity.User;
import com.cinetrackbackend.backend.entity.UserMovie;
import java.util.List;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, Long> {

    Optional<UserMovie> findByUserAndMovie(User user, Movie movie);

    List<UserMovie> findByUser(User user);

    List<UserMovie> findByUserAndWatchList(User user, boolean watchList);

    List<UserMovie> findByUserAndFavourite(User user, boolean favourite);

    boolean existsByUserAndMovie(User user, Movie movie);

    void deleteByUserAndMovie(User user, Movie movie);
}
