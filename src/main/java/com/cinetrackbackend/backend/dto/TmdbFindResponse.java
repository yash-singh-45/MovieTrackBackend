package com.cinetrackbackend.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbFindResponse {

    @JsonProperty("movie_results")
    private List<TmdbMovieResult> movieResults;

    @JsonProperty("tv_results")
    private List<TmdbTvResult> tvResults;

    @JsonProperty("person_results")
    private List<Object> personResults;

    @Data
    public static class TmdbMovieResult {

        private Long id;

        private String title;

        @JsonProperty("release_date")
        private String releaseDate;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("vote_average")
        private double voteAverage;

        @JsonProperty("original_language")
        private String originalLanguage;
    }

    @Data
    public static class TmdbTvResult {

        private Long id;

        private String name;

        @JsonProperty("first_air_date")
        private String firstAirDate;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("vote_average")
        private double voteAverage;

        @JsonProperty("original_language")
        private String originalLanguage;
    }
}