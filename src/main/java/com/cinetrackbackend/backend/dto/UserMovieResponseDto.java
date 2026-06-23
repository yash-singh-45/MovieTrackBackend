package com.cinetrackbackend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMovieResponseDto {
    
    String imdbdId;

    boolean watchlist;

    boolean favourite;
}
