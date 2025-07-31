package com.mlib.backend.repository;

import com.mlib.backend.model.UserFollowedArtist;
import com.mlib.backend.model.UserFollowedArtistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowedArtistRepository
        extends JpaRepository<UserFollowedArtist, UserFollowedArtistId> {

    List<UserFollowedArtist> findByIdUserIdOrderByFollowedAtDesc(Long userId);

    boolean existsByIdUserIdAndIdArtistUri(Long userId, String artistUri);

    @Query("SELECT ufa.id.artistUri FROM UserFollowedArtist ufa WHERE ufa.id.userId = :userId")
    List<String> findArtistUrisByUserId(@Param("userId") Long userId);
}