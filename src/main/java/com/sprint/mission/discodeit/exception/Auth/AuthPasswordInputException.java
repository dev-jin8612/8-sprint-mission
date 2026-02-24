package com.sprint.mission.discodeit.exception.Auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class AuthPasswordInputException extends AuthException {

  public AuthPasswordInputException() {
    super(ErrorCode.LOGIN_PASSWORD_FAILED, Map.of("비밀번호 입력이 잘못되었습니다.", ""));
  }
}