package com.cinetrackbackend.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinetrackbackend.backend.entity.Collection;
import com.cinetrackbackend.backend.entity.CollectionMovie;

import java.util.List;
import java.util.Optional;
import com.cinetrackbackend.backend.entity.Movie;



@Repository
public interface CollectionMovieRepository extends JpaRepository<CollectionMovie, Long> {

    List<CollectionMovie> findByCollection(Collection collection);

    Optional<CollectionMovie> findByCollectionAndMovie(Collection collection, Movie movie);

    boolean existsByCollectionAndMovie(Collection collection, Movie movie);

    void deleteByCollectionAndMovie(Collection collection, Movie movie);
}
