package com.mlib.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Song")
@Data
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer songId;

    @Column(name = "song_name", nullable = false)
    private String songName;

    @Column(name = "artist_id")
    private Integer artistId;

    @Column(name = "album_id")
    private Integer albumId;

    @Column(name = "genre")
    private String genre;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "track_uri")
    private String trackUri;

    @Column(name = "is_single")
    private Boolean isSingle;

    @Column(name = "featured_artists")
    private String featuredArtists;

    @Column(name = "track_order")
    private Integer trackOrder;

    @Column(name = "language")
    private String language;

    @Column(name = "release_status")
    private String releaseStatus;
}