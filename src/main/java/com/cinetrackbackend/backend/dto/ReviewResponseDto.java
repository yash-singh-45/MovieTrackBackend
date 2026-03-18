package com.cinetrackbackend.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewResponseDto {

    private String imdbId;
    private String review;
    private Long userId;
    
    private String username;
    
    private int rating;

    private Long reviewId;

    private int likes;

    @Size(max = 50)
    private String header;
}