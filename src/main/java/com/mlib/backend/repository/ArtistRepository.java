package com.mlib.backend.repository;

import com.mlib.backend.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    // Search by name OR alias
    @Query("SELECT DISTINCT a FROM Artist a " +
            "LEFT JOIN ArtistAlias aa ON a.artistId = aa.artistId " +
            "WHERE a.artistName ILIKE %:query% OR aa.aliasName ILIKE %:query%")
    List<Artist> searchArtists(@Param("query") String query);

    // ✅ Custom query for findByIdIn
    @Query("SELECT a FROM Artist a WHERE a.artistId IN :artistIds")
    List<Artist> findByIdIn(@Param("artistIds") List<Integer> artistIds);
}