package com.cinetrackbackend.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cinetrackbackend.backend.dto.TmdbFindResponse;
import com.cinetrackbackend.backend.dto.TmdbSimilarResponseDto;
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
        return resolveTmdbId(imdbId)
                .map(tmdbId -> {
                    String videoUrl = UriComponentsBuilder
                            .fromUriString(baseUrl + "/movie/" + tmdbId + "/videos")
                            .queryParam("api_key", apiKey)
                            .toUriString();

                    TmdbVideoResponse videoResponse = restTemplate
                            .getForObject(videoUrl, TmdbVideoResponse.class);

                    if (videoResponse == null)
                        return null;

                    return videoResponse.getResults().stream()
                            .filter(v -> "Trailer".equals(v.getType())
                                    && "YouTube".equals(v.getSite()))
                            .findFirst()
                            .map(v -> "https://www.youtube.com/embed/" + v.getKey())
                            .orElse(null);
                })
                .orElse(null);
    }

    public Optional<TmdbSimilarResponseDto> getSimilarMovies(Long tmdbId) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString(baseUrl + "/movie/" + tmdbId + "/similar")
                    .queryParam("api_key", apiKey)
                    .toUriString();

            log.info("Calling TMDB Similar API: {}", url);

            return Optional.ofNullable(
                    restTemplate.getForObject(url, TmdbSimilarResponseDto.class));

        } catch (HttpClientErrorException e) {
            log.error("TMDB /similar API error for tmdbId {}: {}", tmdbId, e.getStatusCode());
            return Optional.empty();
        } catch (RestClientException e) {
            log.error("TMDB /similar connection failed for tmdbId {}: {}", tmdbId, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<TmdbSimilarResponseDto> getSimilarMovies(String imdbId) {
        return resolveTmdbId(imdbId).flatMap(this::getSimilarMovies);
    }

    public TmdbSimilarResponseDto getSimilarMoviesByImdbId(String imdbId) {

        try {

            String findUrl = UriComponentsBuilder
                    .fromUriString(baseUrl + "/find/" + imdbId)
                    .queryParam("api_key", apiKey)
                    .queryParam("external_source", "imdb_id")
                    .toUriString();

            TmdbFindResponse findResponse = restTemplate.getForObject(findUrl, TmdbFindResponse.class);

            if (findResponse == null || findResponse.getMovieResults().isEmpty()) {
                return null;
            }

            Long tmdbId = findResponse.getMovieResults().get(0).getId();

            return getSimilarMovies(tmdbId).get();

        } catch (Exception e) {
            log.error("Failed to fetch similar movies for imdbId {}", imdbId, e);
            return null;
        }
    }

    private Optional<Long> resolveTmdbId(String imdbId) {
        try {
            String findUrl = UriComponentsBuilder
                    .fromUriString(baseUrl + "/find/" + imdbId)
                    .queryParam("api_key", apiKey)
                    .queryParam("external_source", "imdb_id")
                    .toUriString();

            TmdbFindResponse response = restTemplate.getForObject(findUrl, TmdbFindResponse.class);

            if (response == null || response.getMovieResults().isEmpty()) {
                log.warn("No TMDB match found for imdbId: {}", imdbId);
                return Optional.empty();
            }

            return Optional.of(response.getMovieResults().get(0).getId());

        } catch (HttpClientErrorException e) {
            log.error("TMDB /find API error for imdbId {}: {}", imdbId, e.getStatusCode());
            return Optional.empty();
        } catch (RestClientException e) {
            log.error("TMDB /find connection failed for imdbId {}: {}", imdbId, e.getMessage());
            return Optional.empty();
        }
    }
}