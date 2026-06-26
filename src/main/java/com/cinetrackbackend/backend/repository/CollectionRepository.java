package com.cinetrackbackend.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinetrackbackend.backend.entity.Collection;
import com.cinetrackbackend.backend.entity.User;

import java.util.List;
import java.util.Optional;



@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Optional<Collection> findByOwnerAndName(User owner, String name);
    
    Optional<Collection> findByPublicId(String publicId);

    List<Collection> findByOwner(User owner);

    boolean existsByPublicId(String publicId);

    boolean existsByOwnerAndName(User owner, String name);
}