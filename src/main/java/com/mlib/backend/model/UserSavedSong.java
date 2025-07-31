package com.mlib.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "user_saved_song")
public class UserSavedSong implements Serializable {

    @EmbeddedId
    private UserSavedSongId id;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    // Many-to-One: User
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    // Many-to-One: Song
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("songId")
    @JoinColumn(name = "song_id", referencedColumnName = "song_id", insertable = false, updatable = false)
    private Song song;

    // Constructors
    public UserSavedSong() {
        this.addedAt = LocalDateTime.now();
    }

    public UserSavedSong(User user, Song song) {
        this();
        this.user = user;
        this.song = song;
        this.id = new UserSavedSongId(user.getUserId(), song.getSongId());
    }

    // Getters and Setters
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

    // equals(), hashCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSavedSong)) return false;
        UserSavedSong that = (UserSavedSong) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return "UserSavedSong{" +
                "id=" + id +
                ", addedAt=" + addedAt +
                '}';
    }
}