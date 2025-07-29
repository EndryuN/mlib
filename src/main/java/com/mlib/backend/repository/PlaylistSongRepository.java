package com.mlib.backend.repository;

import com.mlib.backend.model.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    List<PlaylistSong> findByPlaylistIdOrderByTrackOrder(Long playlistId);

    void deleteByPlaylistIdAndSongId(Long playlistId, Long songId);

    boolean existsByPlaylistIdAndSongId(Long playlistId, Long songId);

    @Query("SELECT MAX(ps.trackOrder) FROM PlaylistSong ps WHERE ps.playlistId = :playlistId")
    Integer findMaxTrackOrderByPlaylistId(@Param("playlistId") Long playlistId);
}