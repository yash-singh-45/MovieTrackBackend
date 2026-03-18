package com.cinetrackbackend.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cinetrackbackend.backend.dto.ReviewRequestDto;
import com.cinetrackbackend.backend.dto.ReviewResponseDto;
import com.cinetrackbackend.backend.entity.Review;
import com.cinetrackbackend.backend.entity.User;
import com.cinetrackbackend.backend.repository.ReviewRepository;
import com.cinetrackbackend.backend.repository.UserRepository;
import com.cinetrackbackend.backend.service.ReviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ReviewResponseDto saveReview(ReviewRequestDto reviewRequest) {
        User user = userRepository.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exists"));

        Optional<Review> existingReview = getReviewByUserIdAndImdbId(user, reviewRequest.getImdbId());

        if (existingReview.isPresent()) {
            throw new RuntimeException("User has already reviewed this movie");
        }

        Review review = new Review();

        review.setUser(user);
        review.setImdbId(reviewRequest.getImdbId());
        review.setReview(reviewRequest.getReview());
        review.setHeader(reviewRequest.getHeader());
        review.setRating(reviewRequest.getRating());

        return modelMapper.map(reviewRepository.save(review), ReviewResponseDto.class);
    }

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    @Override
    public List<ReviewResponseDto> getReviewsByMovie(String imdbId) {
        return reviewRepository.findByImdbIdWithUser(imdbId) // Use the JOIN FETCH method
                .stream()
                .map(review -> {
                    ReviewResponseDto dto = modelMapper.map(review, ReviewResponseDto.class);

                    if (review.getUser() != null) {
                        dto.setUserId(review.getUser().getId());
                        dto.setUsername(review.getUser().getUsername());
                    }

                    return dto;
                })
                .toList();
    }

    @Override
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUserId(user.getId());
    }

    @Override
    public Optional<Review> getReviewByUserIdAndImdbId(User user, String imdbId) {
        return reviewRepository.findByUserIdAndImdbId(user.getId(), imdbId);
    }

    @Override
    @Transactional
    public Review likeReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review does not exist"));

        review.setLikes(review.getLikes() + 1);
        reviewRepository.save(review);
        return review;
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review is not found"));

        reviewRepository.delete(review);
    }

}
