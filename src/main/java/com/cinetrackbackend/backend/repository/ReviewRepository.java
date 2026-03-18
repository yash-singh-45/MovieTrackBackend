package com.cinetrackbackend.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cinetrackbackend.backend.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.imdbId = :imdbId")
    List<Review> findByImdbIdWithUser(@Param("imdbId") String imdbId);

    List<Review> findByImdbId(String imdbId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByUserIdAndImdbId(Long userId, String imdbId);
}
