package com.mlib.backend.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_saved_song")
public class UserSavedSong implements Serializable {
    @EmbeddedId
    private UserSavedSongId id;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    // Many-to-One relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;

    // Constructors, getters, setters
    public UserSavedSong() {}

    public UserSavedSongId getId() {
        return id;
    }

    public void setId(UserSavedSongId id) {
        this.id = id;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}

// Composite primary key class
@Embeddable
class UserSavedSongId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "song_id")
    private Long songId;

    // Constructors, getters, setters
    public UserSavedSongId() {}

    public UserSavedSongId(Long userId, Long songId) {
        this.userId = userId;
        this.songId = songId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

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