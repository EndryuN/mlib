package com.mlib.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserSavedAlbumId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "album_id")
    private Integer albumId;

    // Constructors
    public UserSavedAlbumId() {}

    public UserSavedAlbumId(Long userId, Integer albumId) {
        this.userId = userId;
        this.albumId = albumId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSavedAlbumId)) return false;
        UserSavedAlbumId that = (UserSavedAlbumId) o;
        return userId.equals(that.userId) && albumId.equals(that.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, albumId);
    }
}