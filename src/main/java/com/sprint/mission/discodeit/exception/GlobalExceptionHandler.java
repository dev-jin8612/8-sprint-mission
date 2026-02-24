package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 커스텀 예러 처리
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleException(DiscodeitException e) {
    ErrorResponse response = new ErrorResponse(e);
    log.error("커스텀 에러 발생 - {}", e.getErrorCode().getMessage());

    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  // 검증 에러처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e){
    ErrorResponse response = new ErrorResponse(e);
    log.error("검증 에러 발생 - {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  // 기타 예외처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse response = new ErrorResponse(e);
    log.error("기타 에러 발생 - {}",e.getMessage());

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
  }

  @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
  public ResponseEntity<Void> handleNoResource(org.springframework.web.servlet.resource.NoResourceFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }
}
