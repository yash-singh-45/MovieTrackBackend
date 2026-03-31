package com.cinetrackbackend.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TmdbVideo {
    
    @JsonProperty("key")
    private String key;

    @JsonProperty("type")
    private String type;

    @JsonProperty("site")
    private String site;
}