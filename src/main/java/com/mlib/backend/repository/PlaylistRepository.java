package com.mlib.backend.repository;

import com.mlib.backend.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Playlist> findByIsPublicTrueOrderByCreatedAtDesc();

    @Query("SELECT p FROM Playlist p WHERE p.userId = :userId AND p.playlistName LIKE %:name%")
    List<Playlist> findByUserIdAndPlaylistNameContaining(@Param("userId") Long userId, @Param("name") String name);
}