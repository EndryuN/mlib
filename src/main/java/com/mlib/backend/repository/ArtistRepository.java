package com.mlib.backend.repository;


import com.mlib.backend.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    @Query("SELECT a FROM Artist a WHERE a.artistName ILIKE %:query% " +
            "UNION " +
            "SELECT a FROM Artist a JOIN ArtistAlias aa ON a.artistId = aa.artistId " +
            "WHERE aa.aliasName ILIKE %:query%")
    List<Artist> searchArtists(@Param("query") String query);
}