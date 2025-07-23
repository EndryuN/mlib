package com.mlib.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ArtistAlias", uniqueConstraints = @UniqueConstraint(columnNames = {"artist_id", "alias_name"}))
@Data
public class ArtistAlias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aliasId;

    @Column(name = "artist_id")
    private Integer artistId;

    @Column(name = "alias_name", nullable = false)
    private String aliasName;

    @Column(name = "alias_type")
    private String aliasType;

    @Column(name = "locale")
    private String locale;

    @Column(name = "primary_alias")
    private Boolean primaryAlias = false;
}