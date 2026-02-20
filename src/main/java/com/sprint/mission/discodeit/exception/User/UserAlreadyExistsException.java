package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException(Object details) {
    super(ErrorCode.DUPLICATE_USER, Map.of("이미 존재하는 유저 값: ",details));
  }
}
