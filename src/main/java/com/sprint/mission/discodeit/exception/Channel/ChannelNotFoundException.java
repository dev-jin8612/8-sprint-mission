package com.sprint.mission.discodeit.exception.Channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(Object details) {
    super(ErrorCode.CHANNEL_NOT_FOUND,Map.of("채널을 찾을 수 없습니다. : ",details));
  }
}
