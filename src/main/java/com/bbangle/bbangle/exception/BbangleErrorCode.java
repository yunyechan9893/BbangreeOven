package com.bbangle.bbangle.exception;

import java.util.stream.Stream;

public enum BbangleErrorCode {

  UNKNOWN_CATEGORY(-1, "올바르지 않은 Category 입니다. 다시 입력해주세요"),
  DUPLICATE_NICKNAME(-2, "중복된 닉네임이에요!"),
  EXCEED_NICKNAME_LENGTH(-3, "닉네임은 20자 제한이에요!"),

  NOTFOUND_MEMBER(-4, "해당 user가 존재하지 않습니다."),
  NOTFOUND_WISH_INFO(-5, "일치하는 스토어 찜을 찾지못했습니다."),

  AWS_ACL_BLOCK(-6, "S3에 버킷의 ACL권한을 설정해주세요"),
  AWS_ENVIRONMENT(-7, "AWS 환경에서 진행해주세요"),

  INTERNAL_SERVER_ERROR(-999, "서버 내부 에러입니다"),
  ;

  private int code;
  private String message;

  BbangleErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }


  public static BbangleErrorCode of(int code) {
    return Stream.of(BbangleErrorCode.values())
        .filter(message -> message.getCode() == code)
        .findFirst()
        .orElseThrow(BbangleException::new);
  }

  public static BbangleErrorCode of(String message) {
    return Stream.of(BbangleErrorCode.values())
        .filter(error -> error.getMessage().equals(message))
        .findFirst()
        .orElseThrow(BbangleException::new);
  }


}
