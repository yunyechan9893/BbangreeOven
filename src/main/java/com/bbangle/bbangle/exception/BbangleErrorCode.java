package com.bbangle.bbangle.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BbangleErrorCode {

    UNKNOWN_CATEGORY(-1, "올바르지 않은 Category 입니다. 다시 입력해주세요", BAD_REQUEST),
    DUPLICATE_NICKNAME(-2, "중복된 닉네임이에요!", CONFLICT),
    EXCEED_NICKNAME_LENGTH(-3, "닉네임은 20자 제한이에요!", BAD_REQUEST),

    NOTFOUND_MEMBER(-4, "해당 user가 존재하지 않습니다.", NOT_FOUND),
    NOTFOUND_WISH_INFO(-5, "일치하는 스토어 찜을 찾지못했습니다.", NOT_FOUND),

    AWS_ACL_BLOCK(-6, "S3에 버킷의 ACL권한을 설정해주세요", HttpStatus.INTERNAL_SERVER_ERROR),
    AWS_ENVIRONMENT(-7, "AWS 환경에서 진행해주세요", HttpStatus.INTERNAL_SERVER_ERROR),

    PRICE_NOT_OVER_ZERO(-8, "0원 이상의 가격을 입력해주세요", BAD_REQUEST),
    INVALID_CATEGORY(-9, "존재하지 않는 카테고리입니다.", BAD_REQUEST),
    BOARD_NOT_FOUND(-10, "존재하지 않는 게시글입니다.", BAD_REQUEST),
    RANKING_NOT_FOUND(-11, "해당 게시글의 랭킹이 존재하지 않습니다.", BAD_REQUEST),
    INVALID_CURSOR_ID(-12, "유효하지 않은 cursorId 입니다.", BAD_REQUEST),
    NOTIFICATION_NOT_FOUND(-13, "존재하지 않는 공지사항입니다.", BAD_REQUEST),
    STORE_NOT_FOUND(-14, "존재하지 않는 스토어입니다", BAD_REQUEST),
    INTERNAL_SERVER_ERROR(-999, "서버 내부 에러입니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    public static BbangleErrorCode of(int code) {
        return Stream.of(BbangleErrorCode.values())
            .filter(message -> message.getCode() == code)
            .findFirst()
            .orElseThrow(BbangleException::new);
    }

    public static BbangleErrorCode of(String message) {
        return Stream.of(BbangleErrorCode.values())
            .filter(error -> error.getMessage()
                .equals(message))
            .findFirst()
            .orElseThrow(BbangleException::new);
    }


}
