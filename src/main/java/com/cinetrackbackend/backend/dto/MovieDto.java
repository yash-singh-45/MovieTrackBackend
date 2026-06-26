package com.cinetrackbackend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private String imdbId;

    private String title;

    private String posterPath;

    private Double rating;
}
