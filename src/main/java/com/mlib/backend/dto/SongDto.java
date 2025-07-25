package com.mlib.backend.dto;

import lombok.Data;

@Data
public class SongDto {
    private Integer songId;
    private String songName;

    // Artist info (joined from Artist table)
    private Integer artistId;
    private String artistName;  // ✅ Added artistName
    private Boolean artistVerifiedCheck;

    // Album info (joined from Album table)
    private Integer albumId;
    private String albumName;  // ✅ Added albumName
    private String albumDate;

    // Song metadata
    private String genre;
    private Long durationMs;
    private String durationFormatted; // e.g., "3:53"
    private String trackUri;
    private Boolean isSingle;
    private String featuredArtists; // comma-separated: "Daddy Yankee, Luis Fonsi"
    private Integer trackOrder;
    private String language; // ISO 639-3: "eng", "jpn"
    private String languageDisplayName; // e.g., "English", "Japanese"
    private String releaseStatus; // "official", "likely_official"
}