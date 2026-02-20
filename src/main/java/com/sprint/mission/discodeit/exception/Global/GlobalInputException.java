package com.sprint.mission.discodeit.exception.Global;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class GlobalInputException extends GlobalException {

  public GlobalInputException(Object details) {
    super(ErrorCode.WRONG_INPUT, Map.of("입력이 잘못되었습니다. : ", details));
  }
}