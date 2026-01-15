package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UUID> participantIds,
    LocalDateTime lastMessageAt
) {

  @Override
  public String toString() {
    return name;
  }
}
