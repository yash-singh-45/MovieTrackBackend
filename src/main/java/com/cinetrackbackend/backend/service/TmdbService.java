package com.cinetrackbackend.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cinetrackbackend.backend.dto.TmdbFindResponse;
import com.cinetrackbackend.backend.dto.TmdbVideoResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public TmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTrailerByImdbId(String imdbId) {
        try {
            // Step 1: IMDb ID → TMDB ID
            String findUrl = UriComponentsBuilder
                .fromUriString(baseUrl + "/find/" + imdbId)
                .queryParam("api_key", apiKey)
                .queryParam("external_source", "imdb_id")
                .toUriString();

            TmdbFindResponse findResponse = restTemplate
                .getForObject(findUrl, TmdbFindResponse.class);

            if (findResponse == null || findResponse.getMovieResults().isEmpty()) {
                return null;
            }

            Long tmdbId = findResponse.getMovieResults().get(0).getId();

            // Step 2: TMDB ID → trailer key
            String videoUrl = UriComponentsBuilder
                .fromUriString(baseUrl + "/movie/" + tmdbId + "/videos")
                .queryParam("api_key", apiKey)
                .toUriString();

            TmdbVideoResponse videoResponse = restTemplate
                .getForObject(videoUrl, TmdbVideoResponse.class);

            if (videoResponse == null) return null;

            return videoResponse.getResults().stream()
                .filter(v -> "Trailer".equals(v.getType()) 
                          && "YouTube".equals(v.getSite()))
                .findFirst()
                .map(v -> "https://www.youtube.com/embed/" + v.getKey())
                .orElse(null);

        } catch (HttpClientErrorException e) {
            log.error("TMDB API error for imdbId {}: {}", imdbId, e.getStatusCode());
            return null;
        } catch (RestClientException e) {
            log.error("TMDB connection failed: {}", e.getMessage());
            return null;
        }
    }
}