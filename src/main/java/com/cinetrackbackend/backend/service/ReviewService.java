package com.cinetrackbackend.backend.service;

import java.util.List;
import java.util.Optional;

import com.cinetrackbackend.backend.dto.ReviewRequestDto;
import com.cinetrackbackend.backend.dto.ReviewResponseDto;
import com.cinetrackbackend.backend.entity.Review;
import com.cinetrackbackend.backend.entity.User;


public interface ReviewService {

    ReviewResponseDto saveReview(ReviewRequestDto review);

    Optional<Review> getReviewById(Long reviewId);

    List<ReviewResponseDto> getReviewsByMovie(String imdbId);

    List<Review> getReviewsByUser(User user);

    Optional<Review> getReviewByUserIdAndImdbId( User user, String imdbId);

    Review likeReview(Long reviewId);

    void deleteReview(Long reviewId);
} 