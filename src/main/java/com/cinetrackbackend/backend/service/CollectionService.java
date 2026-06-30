package com.cinetrackbackend.backend.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cinetrackbackend.backend.dto.CollectionRequestDto;
import com.cinetrackbackend.backend.dto.CollectionResponseDto;
import com.cinetrackbackend.backend.entity.Collection;
import com.cinetrackbackend.backend.entity.User;
import com.cinetrackbackend.backend.repository.CollectionRepository;
import com.cinetrackbackend.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CollectionResponseDto createCollection(CollectionRequestDto requestCollection, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (collectionRepository.existsByOwnerAndName(user, requestCollection.getName())) {
            throw new RuntimeException("Same name collections exists");
        }

        Collection collection = new Collection();

        String publicId = UIdGenerator.generateId(15);
        while( collectionRepository.existsByPublicId(publicId)){
            publicId= UIdGenerator.generateId(15);
        }

        collection.setPublicId(publicId);
        collection.setBio(requestCollection.getBio());
        collection.setName(requestCollection.getName());
        collection.setOwner(user);
        collection.setPrivate(requestCollection.isPrivate());
        collection.setMovieCount(0);
        collectionRepository.save(collection);

        CollectionResponseDto responseDto = modelMapper.map(collection, CollectionResponseDto.class);
        responseDto.setUsername(username);

        return responseDto;

    }

    public List<CollectionResponseDto> getCollection(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        List<Collection> collections = collectionRepository.findByOwner(user);

        List<CollectionResponseDto> response = collections.stream()
                .map((collection) -> modelMapper.map(collection, CollectionResponseDto.class))
                .toList();

        for(CollectionResponseDto res: response){
            res.setUsername(username);
        }

        return response;
    }

    @Transactional
    public CollectionResponseDto deleteCollection(Long id, String username) {

        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        if (!collection.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to delete this collection");
        }

        CollectionResponseDto response = modelMapper.map(collection, CollectionResponseDto.class);
        response.setUsername(username);
        
        collectionRepository.delete(collection);

        return response;
    }

    @Transactional
    public CollectionResponseDto getCollectionInfo(String publicId){
        Collection collection = collectionRepository.findByPublicId(publicId)
        .orElseThrow(() -> new RuntimeException("Collection not found"));

        CollectionResponseDto responseDto = new CollectionResponseDto();

        responseDto.setId(collection.getId());
        responseDto.setPublicId(collection.getPublicId());
        responseDto.setBio(collection.getBio());
        responseDto.setMovieCount(collection.getMovieCount());
        responseDto.setUsername(collection.getOwner().getUsername());
        responseDto.setName(collection.getName());
        responseDto.setPrivate(collection.isPrivate());

        return responseDto;
    }
    
}
