package com.cinetrackbackend.backend.service;

import com.cinetrackbackend.backend.entity.Collection;
import com.cinetrackbackend.backend.entity.CollectionMovie;
import com.cinetrackbackend.backend.entity.Movie;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cinetrackbackend.backend.dto.CollectionMovieRequestDto;
import com.cinetrackbackend.backend.dto.MovieDto;
import com.cinetrackbackend.backend.repository.CollectionMovieRepository;
import com.cinetrackbackend.backend.repository.CollectionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CollectionMovieService {
    private final CollectionMovieRepository collectionMovieRepository;
    private final CollectionRepository collectionRepository;
    private final MovieService movieService;
    private final ModelMapper modelMapper;

    public void addMovieToCollection(CollectionMovieRequestDto requestData, String username){
        Collection collection = collectionRepository.findById(requestData.getCollectionId())
        .orElseThrow( ()-> new RuntimeException("Collection not found"));

        String collectionOwner = collection.getOwner().getUsername();
        if(!username.equals(collectionOwner)){
            throw new RuntimeException("Bad Request!!");
        }

        MovieDto movieDto = modelMapper.map(requestData, MovieDto.class);

        Movie movie = movieService.getOrCreateMovie(movieDto);

        if(collectionMovieRepository.existsByCollectionAndMovie(collection, movie)){
            throw new RuntimeException("Movie already in collection");
        }

        CollectionMovie collectionMovie = new CollectionMovie();

        collectionMovie.setCollection(collection);
        collectionMovie.setMovie(movie);

        collectionMovieRepository.save(collectionMovie);
        collection.setMovieCount(collection.getMovieCount()+1);
        collectionRepository.save(collection);
    }

    public List<MovieDto> getMoviesFromCollection(String publicId, String username){
        Collection collection = collectionRepository.findByPublicId(publicId)
        .orElseThrow( () -> new RuntimeException("Collection not found!!"));

        if(collection.isPrivate() && !collection.getOwner().getUsername().equals(username)){
            throw new RuntimeException("Restricted Collection by Owner!!");
        }

        List<CollectionMovie> collectionMovies = collectionMovieRepository.findByCollection(collection);

        return collectionMovies.stream().map( (movie)-> modelMapper.map(movie.getMovie(), MovieDto.class)).toList();

    }
}
