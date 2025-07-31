package com.mlib.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "external_url", uniqueConstraints = @UniqueConstraint(
        columnNames = {"entity_type", "entity_id", "platform"}
))
public class ExternalUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private EntityType entityType;  // SONG, ALBUM, ARTIST

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "platform", nullable = false)
    private String platform;  // "spotify", "youtube", etc.

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "uri")
    private String uri;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();

    // Getters, setters, equals, hashCode...
}

enum EntityType {
    SONG, ALBUM, ARTIST, PLAYLIST
}


