package com.bbangle.bbangle.dto;

import jakarta.validation.constraints.NotBlank;

public record NoticeUploadRequest (
    @NotBlank
    String title,
    @NotBlank
    String content
){

}
