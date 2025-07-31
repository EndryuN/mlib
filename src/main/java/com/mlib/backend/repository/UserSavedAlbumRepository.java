package com.mlib.backend.repository;

import com.mlib.backend.model.UserSavedAlbum;
import com.mlib.backend.model.UserSavedAlbumId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSavedAlbumRepository extends JpaRepository<UserSavedAlbum, UserSavedAlbumId> {

    List<UserSavedAlbum> findByIdUserIdOrderByAddedAtDesc(Long userId);

    boolean existsByIdUserIdAndIdAlbumId(Long userId, Integer albumId);
}