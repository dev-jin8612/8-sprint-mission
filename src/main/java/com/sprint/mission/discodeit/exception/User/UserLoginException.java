package com.sprint.mission.discodeit.exception.User;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserLoginException extends UserException {

  public UserLoginException(Object details) {
    super(ErrorCode.LOGIN_FAILED, Map.of("비밀번호가 다릅니다.",details));
  }
}
