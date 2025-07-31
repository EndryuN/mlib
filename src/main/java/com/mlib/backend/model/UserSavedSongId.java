package com.mlib.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserSavedSongId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "song_id")
    private Integer songId;

    // Constructors
    public UserSavedSongId() {}

    public UserSavedSongId(Long userId, Integer songId) {
        this.userId = userId;
        this.songId = songId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSavedSongId)) return false;
        UserSavedSongId that = (UserSavedSongId) o;
        return userId.equals(that.userId) && songId.equals(that.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, songId);
    }
}