package com.mlib.backend.repository;

import com.mlib.backend.model.ArtistAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtistAliasRepository extends JpaRepository<ArtistAlias, Integer> {
    List<ArtistAlias> findByArtistId(Integer artistId);
}