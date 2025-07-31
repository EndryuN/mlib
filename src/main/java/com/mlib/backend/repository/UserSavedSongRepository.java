package com.mlib.backend.repository;

import com.mlib.backend.model.UserSavedSong;
import com.mlib.backend.model.UserSavedSongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing UserSavedSong entities.
 * Handles database operations for the user_saved_song table.
 */
@Repository
public interface UserSavedSongRepository extends JpaRepository<UserSavedSong, UserSavedSongId> {

    /**
     * Find all saved songs for a user, ordered by when they were added (newest first).
     *
     * @param userId the ID of the user
     * @return list of UserSavedSong entities
     */
    List<UserSavedSong> findByIdUserIdOrderByAddedAtDesc(Long userId);

    /**
     * Check if a specific song is saved by a specific user.
     *
     * @param userId the ID of the user
     * @param songId the ID of the song
     * @return true if the song is saved by the user, false otherwise
     */
    boolean existsByIdUserIdAndIdSongId(Long userId, Long songId);

    /**
     * Alternative method using @Query annotation (optional, same as derived query above).
     * Useful if you need more control over the query.
     */
    @Query("SELECT COUNT(uss) > 0 FROM UserSavedSong uss WHERE uss.id.userId = :userId AND uss.id.songId = :songId")
    boolean isSongSavedByUser(@Param("userId") Long userId, @Param("songId") Long songId);

    /**
     * Get only the song IDs for a user's saved songs.
     * Useful for frontend to quickly check which songs are saved without loading full entities.
     *
     * @param userId the ID of the user
     * @return list of song IDs
     */
    @Query("SELECT uss.id.songId FROM UserSavedSong uss WHERE uss.id.userId = :userId ORDER BY uss.addedAt DESC")
    List<Long> findSongIdsByUserId(@Param("userId") Long userId);
}