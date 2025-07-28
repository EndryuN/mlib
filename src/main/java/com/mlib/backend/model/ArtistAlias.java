package com.mlib.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Artist_Alias", uniqueConstraints = @UniqueConstraint(columnNames = {"artist_id", "alias_name"}))
@Data
public class ArtistAlias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aliasId;

    @Column(name = "artist_id")
    private Integer artistId;

    @Column(name = "alias_name", nullable = false)
    private String aliasName;

}