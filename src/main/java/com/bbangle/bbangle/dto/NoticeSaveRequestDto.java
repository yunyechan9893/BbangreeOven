package com.bbangle.bbangle.dto;

import jakarta.validation.constraints.NotBlank;

public record NoticeSaveRequestDto(
    @NotBlank
    String title,
    @NotBlank
    String content
) {

}
