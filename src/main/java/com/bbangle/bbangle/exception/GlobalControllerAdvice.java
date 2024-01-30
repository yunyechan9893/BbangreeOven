package com.bbangle.bbangle.exception;

import java.util.List;
import java.util.Objects;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    public static final String DEFAULT_MESSAGE = "유효하지 않은 요청 입니다.";

    @ExceptionHandler(CategoryTypeException.class)
    public ResponseEntity<ErrorResponse> handleCategoryTpeException(CategoryTypeException ex){
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex){
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NoSuchMemberidOrStoreIdException.class)
    public ResponseEntity<ErrorResponse> handleFailFindWishListStoreException(NoSuchMemberidOrStoreIdException ex){
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex){
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
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

}
