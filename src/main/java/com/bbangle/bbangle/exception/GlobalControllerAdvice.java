package com.bbangle.bbangle.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

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

}
