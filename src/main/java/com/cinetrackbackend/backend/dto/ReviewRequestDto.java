package com.cinetrackbackend.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewRequestDto {

    private String imdbId;

    @Size(min = 100, max = 500, message = "Review must be between 100 and 500 characters")
    private String review;

    private Long userId;
    
    @Min(value = 1, message = "Rating should be at least 1")
    @Max(value = 10, message = "Rating should be no more than 10")
    private int rating;

    @Size(max = 100)
    private String header;
}
