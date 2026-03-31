package com.cinetrackbackend.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TmdbVideoResponse {
    
    @JsonProperty("results")
    private List<TmdbVideo> results;

}