package com.mlib.backend.repository;

import com.mlib.backend.model.UserSavedSong;
import com.mlib.backend.model.UserSavedSongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSavedSongRepository extends JpaRepository<UserSavedSong, UserSavedSongId> {
    List<UserSavedSong> findByIdUserIdOrderByAddedAtDesc(Long userId);

    boolean existsByIdUserIdAndIdSongId(Long userId, Long songId);
}