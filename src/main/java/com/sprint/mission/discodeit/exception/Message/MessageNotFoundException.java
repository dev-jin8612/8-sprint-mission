package com.sprint.mission.discodeit.exception.Message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(Object details) {
    super(ErrorCode.MESSAGE_NOT_FOUND, Map.of("메세지가 없습니다. : ",details));
  }
}