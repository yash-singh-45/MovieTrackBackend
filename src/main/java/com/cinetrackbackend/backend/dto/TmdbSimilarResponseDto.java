package com.cinetrackbackend.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbSimilarResponseDto {

    private int page;

    private List<TmdbMovieResult> results;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    @Data
    public static class TmdbMovieResult {

        private Long id;

        private String title;

        @JsonProperty("original_title")
        private String originalTitle;

        private String overview;

        @JsonProperty("release_date")
        private String releaseDate;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("backdrop_path")
        private String backdropPath;

        @JsonProperty("vote_average")
        private double voteAverage;

        @JsonProperty("vote_count")
        private int voteCount;

        private double popularity;

        @JsonProperty("original_language")
        private String originalLanguage;

        @JsonProperty("genre_ids")
        private List<Integer> genreIds;

        private boolean adult;

        private boolean video;
    }
}