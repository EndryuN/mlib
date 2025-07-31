package com.mlib.backend.repository;

import com.mlib.backend.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {

    @Query("SELECT a FROM Album a WHERE a.albumId IN :albumIds")
    List<Album> findByIdIn(@Param("albumIds") List<Integer> albumIds);
}