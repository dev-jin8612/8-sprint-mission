package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(Object args) {
    super(ErrorCode.USER_NOT_FOUND, Map.of("유저를 찾을 수 없습니다. : ",args));
  }
}
