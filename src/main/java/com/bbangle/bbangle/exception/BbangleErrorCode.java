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
    INVALID_FOLDER_TITLE(-14, "유효하지 않은 폴더 제목입니다.", BAD_REQUEST),
    OVER_MAX_FOLDER(-15, "10개를 초과한 폴더를 생성하실 수 없습니다.", BAD_REQUEST),
    FOLDER_NAME_ALREADY_EXIST(-16, "이미 존재하는 폴더 이름은 다시 사용하실 수 없습니다.", BAD_REQUEST),
    INVALID_FOLDER_MEMBER(-17, "폴더 생성 시 멤버 정보는 필수입니다.", BAD_REQUEST),
    FOLDER_NOT_FOUND(-18, "해당 폴더를 찾을 수 없습니다.", BAD_REQUEST),
    DEFAULT_FOLDER_NAME_CANNOT_CHNAGE(-19, "기본 폴더는 이름을 변경할 수 없습니다.", BAD_REQUEST),
    ALREADY_ON_WISHLIST(-20, "이미 위시리스트에 존재하는 게시글입니다.", BAD_REQUEST),
    WISHLIST_BOARD_NOT_FOUND(-21, "해당 게시글 찜 내역을 찾을 수 없습니다.", BAD_REQUEST),
    WISHLIST_BOARD_ALREADY_CANCELED(-22, "이미 찜 게시글에서 삭제하였습니다.", BAD_REQUEST),
    CANNOT_DELETE_DEFAULT_FOLDER(-23, "기본 폴더는 삭제할 수 없습니다.", BAD_REQUEST),
    FOLDER_ALREADY_DELETED(-24, "이미 삭제된 폴더는 다시 삭제할 수 없습니다.", BAD_REQUEST),
    CANNOT_UPDATE_ALREADY_DELETED_FOLDER(-25, "이미 삭제된 폴더는 변경할 수 없습니다.", BAD_REQUEST),
    FOLDER_ID_MUST_NOT_NULL(-26, "폴더 아이디는 반드시 포함되어야 합니다.", BAD_REQUEST),
    STORE_NOT_FOUND(-27, "존재하지 않는 스토어입니다", BAD_REQUEST),

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
