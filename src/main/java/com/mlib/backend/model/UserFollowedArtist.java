package com.mlib.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_followed_artist")
public class UserFollowedArtist implements Serializable {

    @EmbeddedId
    private UserFollowedArtistId id;

    @Column(name = "followed_at")
    private LocalDateTime followedAt;

    // Many-to-One: User
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    // No direct Artist relationship — we use artist_uri as logical key

    public UserFollowedArtist() {
        this.followedAt = LocalDateTime.now();
    }

    public UserFollowedArtist(User user, String artistUri) {
        this();
        this.user = user;
        this.id = new UserFollowedArtistId(user.getUserId(), artistUri);
    }

    // Getters and setters
    public UserFollowedArtistId getId() {
        return id;
    }

    public void setId(UserFollowedArtistId id) {
        this.id = id;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // equals(), hashCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollowedArtist)) return false;
        UserFollowedArtist that = (UserFollowedArtist) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserFollowedArtist{" +
                "id=" + id +
                ", followedAt=" + followedAt +
                '}';
    }
}