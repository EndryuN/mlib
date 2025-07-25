package com.mlib.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArtistDto {
    private Integer artistId;
    private String artistName;
    private String artistUri;
    private Boolean verifiedCheck;
    private List<AliasDto> aliases;
}