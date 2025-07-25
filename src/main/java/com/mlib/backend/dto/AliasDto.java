package com.mlib.backend.dto;

import lombok.Data;

@Data
public class AliasDto {
    private Integer aliasId;
    private String aliasName;
    private String aliasType;
    private String locale;
    private Boolean primaryAlias;
}