package com.cinetrackbackend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResponseDto {
    private Long id;
    private String username;
    private String name;
    private String bio;
    private String publicId;
    private boolean isPrivate;
    private int movieCount;
}
