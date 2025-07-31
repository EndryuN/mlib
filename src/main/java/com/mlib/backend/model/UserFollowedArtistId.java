package com.mlib.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserFollowedArtistId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "artist_uri")
    private String artistUri;

    public UserFollowedArtistId() {}

    public UserFollowedArtistId(Long userId, String artistUri) {
        this.userId = userId;
        this.artistUri = artistUri;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getArtistUri() {
        return artistUri;
    }

    public void setArtistUri(String artistUri) {
        this.artistUri = artistUri;
    }

    // equals(), hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollowedArtistId)) return false;
        UserFollowedArtistId that = (UserFollowedArtistId) o;
        return userId.equals(that.userId) && artistUri.equals(that.artistUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, artistUri);
    }
}