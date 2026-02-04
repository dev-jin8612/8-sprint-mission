package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record LoginRequest(
    String username,
    String password
) {

  public static record MessageCreateRequest(
          String content,
          UUID channelId,
          UUID authorId
  ) {
  }
}
