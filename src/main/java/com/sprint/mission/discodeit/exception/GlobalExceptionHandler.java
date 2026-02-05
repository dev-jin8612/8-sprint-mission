package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleException(DiscodeitException e) {
    ErrorResponse response = new ErrorResponse(e);
    log.error("에러 발생 - ", e.getMessage());

    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e,
      HttpServletRequest request
  ) {
    ErrorCode code = GlobalErrorCode.INVALID_INPUT;

    // 에러 필드 정보 추출
    List<ErrorResponse.Detail> details = e.getBindingResult().getFieldErrors().stream()
        .map(error -> new ErrorResponse.Detail(
            error.getField(),
            error.getDefaultMessage(),
            error.getRejectedValue()
        ))
        .toList();

    log.error("유효성 검사 실패 : code={}, message={}, path={}, details={}", code.getCode(), code.getMessage(), request.getRequestURI(), details);

    ErrorResponse response = ErrorResponse.of(code, code.getMessage(), request.getRequestURI(), details);

    return ResponseEntity.status(code.getHttpStatus()).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse response = new ErrorResponse(e);
    log.error("에러 발생 - ",e.getMessage());

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
  }
}
