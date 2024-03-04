package com.bbangle.bbangle.exception;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    public static final String DEFAULT_MESSAGE = "유효하지 않은 요청 입니다.";

    @ExceptionHandler(CategoryTypeException.class)
    public ResponseEntity<ErrorResponse> handleCategoryTpeException(CategoryTypeException ex) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NoSuchMemberidOrStoreIdException.class)
    public ResponseEntity<ErrorResponse> handleFailFindWishListStoreException(
        NoSuchMemberidOrStoreIdException ex
    ) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        List<FieldError> errors = e.getFieldErrors();
        String message = errors.stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(DEFAULT_MESSAGE);

        ErrorResponse errorResponse = new ErrorResponse(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    @ExceptionHandler(ExceedNicknameLengthException.class)
    public ResponseEntity<ErrorResponse> handle(ExceedNicknameLengthException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> handle(DuplicateNicknameException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(e.getMessage()));
    }

    //아마존 S3 ACL 권한 설정 안했을 시 에러 발생
    @ExceptionHandler(value = AmazonS3Exception.class)
    public ResponseEntity<ErrorResponse> amazonS3Exception(Exception e) {
        String ERROR_MESSAGE = "S3에 버킷의 ACL권한을 설정해주세요";
        log.error(String.format("%s:\n%s", e, ERROR_MESSAGE));

        return ResponseEntity.internalServerError()
            .body(ErrorResponse.builder()
                .message(ERROR_MESSAGE)
                .build());
    }


    @ExceptionHandler(value = SdkClientException.class)
    public ResponseEntity<ErrorResponse> sdkClientException(Exception e) {
        // build.gradle에, spring-cloud-starter-aws 의존성 주입시
        // 로컬환경은, aws환경이 아니기때문에 나는 에러
        String ERROR_MESSAGE = "AWS 환경에서 진행해주세요";
        log.error(String.format("%s:\n%s", e, ERROR_MESSAGE));

        return ResponseEntity.internalServerError()
            .body(ErrorResponse.builder()
                .message(ERROR_MESSAGE)
                .build());
    }

}
