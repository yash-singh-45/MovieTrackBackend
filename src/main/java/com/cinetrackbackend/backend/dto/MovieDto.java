package com.cinetrackbackend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    public String imdbId;

    public String title;

    public String posterPath;

    public Double rating;
}
