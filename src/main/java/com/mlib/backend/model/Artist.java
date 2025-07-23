package com.mlib.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Artist", uniqueConstraints = @UniqueConstraint(columnNames = "artist_name"))
@Data
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistId;

    @Column(name = "artist_name", nullable = false)
    private String artistName;

    @Column(name = "artist_uri")
    private String artistUri;

    @Column(name = "verified_check")
    private Boolean verifiedCheck;
}