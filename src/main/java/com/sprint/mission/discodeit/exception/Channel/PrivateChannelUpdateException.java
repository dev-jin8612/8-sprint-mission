package com.sprint.mission.discodeit.exception.Channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException(Object details) {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE, Map.of("비공개 채널은 수정 불가 입니다. : ",details));
  }
}
