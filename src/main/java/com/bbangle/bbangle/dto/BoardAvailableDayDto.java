package com.bbangle.bbangle.dto;

import lombok.Builder;

@Builder
public record BoardAvailableDayDto(
    Boolean mon,
    Boolean tue,
    Boolean wed,
    Boolean thu,
    Boolean fri,
    Boolean sat,
    Boolean sun
) {

}
