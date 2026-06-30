package com.cinetrackbackend.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinetrackbackend.backend.dto.CollectionRequestDto;
import com.cinetrackbackend.backend.dto.CollectionResponseDto;
import com.cinetrackbackend.backend.service.CollectionService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping("/create")
    public ResponseEntity<CollectionResponseDto> createCollection(@RequestBody CollectionRequestDto requestDto,
            Authentication authentication) {

        String username = authentication.getName();

        CollectionResponseDto response = collectionService.createCollection(requestDto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<CollectionResponseDto> deleteCollection(@RequestBody Long id, Authentication authentication) {

        String username = authentication.getName();

        CollectionResponseDto response = collectionService.deleteCollection(id, username);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<CollectionResponseDto>> getMethodName(Authentication authentication) {
        String username = authentication.getName();

        List<CollectionResponseDto> collections = collectionService.getCollection(username);

        return ResponseEntity.ok(collections);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<CollectionResponseDto> getCollectionInfo(@PathVariable String publicId) {
        CollectionResponseDto responseDto = collectionService.getCollectionInfo(publicId);

        return ResponseEntity.ok(responseDto);
    }
}
