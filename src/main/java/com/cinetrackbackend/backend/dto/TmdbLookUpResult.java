package com.cinetrackbackend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TmdbLookUpResult {

    private Long tmdbId;

    private String originalLanguage;
}