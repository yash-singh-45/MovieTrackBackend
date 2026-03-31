package com.cinetrackbackend.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TmdbMovie {

    @JsonProperty("title")
    private String title;

    @JsonProperty("id")
    private Long id;
}
