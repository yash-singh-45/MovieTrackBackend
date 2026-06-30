package com.cinetrackbackend.backend.dto;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class TmdbVideoResponse {

    private Long id;
    private List<VideoResult> results;

    @Data
    public static class VideoResult {

        private String id;
        private String key;
        private String name;
        private String site;
        private String type;
        private boolean official;
        private int size;

        @JsonProperty("published_at")
        private String publishedAt;
    }
}