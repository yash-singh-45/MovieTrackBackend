package com.cinetrackbackend.backend.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.cinetrackbackend.backend.dto.TmdbFindResponse;
import com.cinetrackbackend.backend.dto.TmdbLookUpResult;
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

    public String getTrailerByImdbId(String imdbId, String type) {

        System.out.println("GetTrailer Function Called");
        return resolveTmdbId(imdbId, type)
                .map(tmdbLookUp -> {
                    String videoUrl = UriComponentsBuilder
                            .fromUriString(baseUrl + "/" + type + "/" + tmdbLookUp.getTmdbId() + "/videos")
                            .queryParam("api_key", apiKey)
                            .toUriString();

                            System.out.println("VideoUrl" + videoUrl);
                    TmdbVideoResponse videoResponse = restTemplate.getForObject(videoUrl, TmdbVideoResponse.class);

                    if (videoResponse == null) {
                        return null;
                    }

                    return videoResponse.getResults().stream()
                            .filter(v -> "Trailer".equals(v.getType())
                                    && "YouTube".equals(v.getSite()))
                            .findFirst()
                            .map(v -> "https://www.youtube.com/embed/" + v.getKey())
                            .orElse(null);
                })
                .orElse(null);
    }

    public Optional<TmdbSimilarResponseDto> getSimilarMovies(TmdbLookUpResult lookup, String type) {

        Long tmdbId = lookup.getTmdbId();
        String og_lang = lookup.getOriginalLanguage();

        try {
            String rec_url = UriComponentsBuilder
                    .fromUriString(baseUrl + "/" + type + "/" + tmdbId + "/recommendations")
                    .queryParam("api_key", apiKey)
                    .toUriString();

            String similar_url = UriComponentsBuilder
                    .fromUriString(baseUrl + "/" + type + "/" + tmdbId + "/similar")
                    .queryParam("api_key", apiKey)
                    .toUriString();

            TmdbSimilarResponseDto recommendations = restTemplate.getForObject(rec_url, TmdbSimilarResponseDto.class);

            TmdbSimilarResponseDto similar = restTemplate.getForObject(similar_url, TmdbSimilarResponseDto.class);

            List<TmdbSimilarResponseDto.TmdbMovieResult> all = new ArrayList<>();

            if (recommendations != null && recommendations.getResults() != null)
                all.addAll(recommendations.getResults());

            if (similar != null && similar.getResults() != null)
                all.addAll(similar.getResults());

            // Remove duplicates
            Map<Long, TmdbSimilarResponseDto.TmdbMovieResult> unique = new LinkedHashMap<>();
            for (TmdbSimilarResponseDto.TmdbMovieResult movie : all) {
                unique.putIfAbsent(movie.getId(), movie);
            }

            // Keep only same-language movies
            List<TmdbSimilarResponseDto.TmdbMovieResult> filtered = unique.values()
                    .stream()
                    .filter(movie -> og_lang.equals(movie.getOriginalLanguage()))
                    .toList();

            TmdbSimilarResponseDto response = new TmdbSimilarResponseDto();
            response.setResults(filtered);
            response.setTotalResults(filtered.size());
            response.setPage(1);
            response.setTotalPages(1);

            return Optional.of(response);

        } catch (HttpClientErrorException e) {
            log.error("TMDB /similar API error for tmdbId {}: {}", tmdbId, e.getStatusCode());
            return Optional.empty();
        } catch (RestClientException e) {
            log.error("TMDB /similar connection failed for tmdbId {}: {}", tmdbId, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<TmdbSimilarResponseDto> getSimilarMovies(String imdbId, String type) {
        return resolveTmdbId(imdbId, type)
                .flatMap(lookup -> getSimilarMovies(lookup, type));
    }

    private Optional<TmdbLookUpResult> resolveTmdbId(String imdbId, String type) {
        try {
            String findUrl = UriComponentsBuilder
                    .fromUriString(baseUrl + "/find/" + imdbId)
                    .queryParam("api_key", apiKey)
                    .queryParam("external_source", "imdb_id")
                    .toUriString();

            System.out.println("FIndUrl : " + findUrl);

            TmdbFindResponse response = restTemplate.getForObject(findUrl, TmdbFindResponse.class);

            if (response == null) {
                return Optional.empty();
            }

            if ("movie".equalsIgnoreCase(type)) {
                if (response.getMovieResults() == null || response.getMovieResults().isEmpty()) {
                    log.warn("No movie found for imdbId: {}", imdbId);
                    return Optional.empty();
                }

                return Optional.of(new TmdbLookUpResult(
                        response.getMovieResults().get(0).getId(),
                        response.getMovieResults().get(0).getOriginalLanguage()));
            }

            if ("tv".equalsIgnoreCase(type)) {
                if (response.getTvResults() == null || response.getTvResults().isEmpty()) {
                    log.warn("No TV show found for imdbId: {}", imdbId);
                    return Optional.empty();
                }

                return Optional.of(new TmdbLookUpResult(
                        response.getTvResults().get(0).getId(),
                        response.getTvResults().get(0).getOriginalLanguage()));
            }

            log.warn("Unsupported media type: {}", type);
            return Optional.empty();

        } catch (HttpClientErrorException e) {
            log.error("TMDB /find API error for imdbId {}: {}", imdbId, e.getStatusCode());
            return Optional.empty();
        } catch (RestClientException e) {
            log.error("TMDB /find connection failed for imdbId {}: {}", imdbId, e.getMessage());
            return Optional.empty();
        }
    }

    public String searchPersons(String query) {
        String url = "https://api.themoviedb.org/3/search/person"
                + "?api_key=" + apiKey
                + "&query=" + query
                + "&include_adult=false"
                + "&language=en-US"
                + "&page=1";

        return restTemplate.getForObject(url, String.class);
    }

    public String getHeroMovies() {
        String url = "https://api.themoviedb.org/3/trending/all/week" + "?api_key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }

    public String getImdbId(String tmdbId, String mediaType) {

        String url = "https://api.themoviedb.org/3/"
        + mediaType + "/"
        + tmdbId
        + "/external_ids"
        + "?api_key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }

    public String getTmdbId(String query) {
        String url = "https://api.themoviedb.org/3/search/person"
                + "?api_key=" + apiKey
                + "&query=" + UriUtils.encode(query, StandardCharsets.UTF_8)
                + "&include_adult=false"
                + "&language=en-US"
                + "&page=1";

        return restTemplate.getForObject(url, String.class);
    }

    public String getCelebsData(String tmdbid) {
        String url = "https://api.themoviedb.org/3/person/" +
                tmdbid +
                "?api_key=" + apiKey + "&append_to_response=combined_credits,external_ids";

        return restTemplate.getForObject(url, String.class);
    }
}