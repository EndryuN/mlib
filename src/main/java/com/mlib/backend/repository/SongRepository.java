package com.mlib.backend.repository;

import com.mlib.backend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {
    List<Song> findByArtistId(Integer artistId);

    @Query("SELECT s FROM Song s WHERE s.songName ILIKE %:query% OR s.featuredArtists ILIKE %:query%")
    List<Song> searchSongs(@Param("query") String query);

    List<Song> findByReleaseStatusIn(List<String> statuses);

    List<Song> findByLanguage(String language);
}