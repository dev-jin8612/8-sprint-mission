package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {

  // 커스텀 에러 처리
  public ErrorResponse(DiscodeitException e) {
    this(
        e.getTimestamp(),
        e.getErrorCode().name(),
        e.getErrorCode().getMessage(),
        e.getDetails(),
        e.getClass().getTypeName(),
        e.getErrorCode().getStatus().value()
    );
  }

  // 입력 검증 실패시 에러 처리
  public ErrorResponse(MethodArgumentNotValidException e) {
    this(
        Instant.now(),
        "WRONG_INPUT",
        "입력이 잘 못되었습니다.",
        Map.of(),
        e.getClass().getTypeName(),
        HttpStatus.BAD_REQUEST.value()
    );
  }

  // 기타 예외 처리
  public ErrorResponse(Exception e) {
    this(
        Instant.now(),
        "INTERNAL_SERVER_ERROR",
        "서버 내부 오류가 발생했습니다.",
        Map.of(),
        e.getClass().getTypeName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
  }
}
