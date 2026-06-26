package com.cinetrackbackend.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CollectionMovieRequestDto {

    @NotNull
    String imdbId;

    @NotNull
    Long collectionId;

    @NotNull
    private String title;

    private String posterPath;

    @NotNull
    private Double rating;
}
