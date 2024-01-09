package com.bbangle.bbangle.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ProductDto(
        String name,
        ProductTagDto tagDto
) { }
