package com.mlib.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Album", uniqueConstraints = @UniqueConstraint(columnNames = {"album_name", "artist_id"}))
@Data
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer albumId;

    @Column(name = "album_name", nullable = false)
    private String albumName;

    @Column(name = "artist_id")
    private Integer artistId;

    @Column(name = "album_date")
    private String albumDate;

    @Column(name = "album_tracks")
    private Integer albumTracks = 0;
}