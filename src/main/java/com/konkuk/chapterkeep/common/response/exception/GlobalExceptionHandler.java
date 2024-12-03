package com.konkuk.chapterkeep.common.response.exception;

import com.konkuk.chapterkeep.common.response.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// RestControllerAdvice 이거 찾아보기
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponseDto> generalExceptionHandler(GeneralException e){
        return new ResponseEntity<>(new ErrorResponseDto(e.getCode(), e.getMessage()), HttpStatusCode.valueOf(e.getCode().getStatus()));
    }

}