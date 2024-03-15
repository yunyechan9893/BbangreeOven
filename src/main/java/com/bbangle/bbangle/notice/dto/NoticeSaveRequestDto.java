package com.bbangle.bbangle.notice.dto;

import jakarta.validation.constraints.NotBlank;

public record NoticeSaveRequestDto(
    @NotBlank
    String title,
    @NotBlank
    String content
) {

}
