package com.sprint.mission.discodeit.exception.UserStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusAlreadyExistsException extends UserStatusException {

  public UserStatusAlreadyExistsException(Object details) {
    super(ErrorCode.DUPLICATE_USERSTATUS, Map.of("이미 존재하는 유저 상태 값: ",details));
  }
}
