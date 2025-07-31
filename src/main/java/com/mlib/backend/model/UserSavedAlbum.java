package com.mlib.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_saved_album")
public class UserSavedAlbum implements Serializable {

    @EmbeddedId
    private UserSavedAlbumId id;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    // Many-to-One: User
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // Many-to-One: Album
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("albumId")
    @JoinColumn(name = "album_id", insertable = false, updatable = false)
    private Album album;

    // Constructors
    public UserSavedAlbum() {}

    public UserSavedAlbum(User user, Album album) {
        this();
        this.user = user;
        this.album = album;
        this.id = new UserSavedAlbumId(user.getUserId(), album.getAlbumId());
        this.addedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UserSavedAlbumId getId() {
        return id;
    }

    public void setId(UserSavedAlbumId id) {
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSavedAlbum)) return false;
        UserSavedAlbum that = (UserSavedAlbum) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}