package com.sprint.mission.discodeit.dto.error;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType,
    int status
) {

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
