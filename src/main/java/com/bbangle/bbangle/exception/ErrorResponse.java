package com.bbangle.bbangle.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
    String message
) {


}
