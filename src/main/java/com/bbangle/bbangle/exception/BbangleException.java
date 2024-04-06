package com.bbangle.bbangle.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BbangleException extends RuntimeException {

  private BbangleErrorCode bbangleErrorCode = BbangleErrorCode.INTERNAL_SERVER_ERROR;
  private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  public BbangleException() {
    super("error");
  }

  public BbangleException(String message) {
    super(message);
  }

  public BbangleException(BbangleErrorCode bbangleErrorCode) {
    super(bbangleErrorCode.getMessage());
    this.bbangleErrorCode = bbangleErrorCode;
  }


  public BbangleException(Throwable cause) {
    super(cause);
  }

  public BbangleException(BbangleErrorCode bbangleErrorCode, HttpStatus httpStatus) {
    super(bbangleErrorCode.getMessage());
    this.bbangleErrorCode = bbangleErrorCode;
    this.httpStatus = httpStatus;
  }

  public BbangleException(String message, Throwable cause) {
    super(message, cause);
  }

  public BbangleException(BbangleErrorCode bbangleErrorCode, Throwable cause) {
    super(bbangleErrorCode.getMessage(), cause);
    this.bbangleErrorCode = bbangleErrorCode;
  }
}
