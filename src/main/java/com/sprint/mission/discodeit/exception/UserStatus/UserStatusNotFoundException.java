package com.sprint.mission.discodeit.exception.UserStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusNotFoundException extends UserStatusException {

  public UserStatusNotFoundException(Object args) {
    super(ErrorCode.USERSTATUS_NOT_FOUND, Map.of("유저상태를 찾을 수 없습니다. : ",args));
  }
}
