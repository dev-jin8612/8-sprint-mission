package com.sprint.mission.discodeit.exception.ReadStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(Object details) {
    super(ErrorCode.READSTATUS_NOT_FOUND, Map.of("읽음 상태가 없습니다. : ",details));
  }
}