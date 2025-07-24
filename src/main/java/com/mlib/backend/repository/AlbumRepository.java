// src/main/java/com/mlib/backend/repository/AlbumRepository.java
package com.mlib.backend.repository;

import com.mlib.backend.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    List<Album> findByArtistId(Integer artistId);
}