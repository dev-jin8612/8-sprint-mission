package com.sprint.mission.discodeit.dto.data;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageDTO(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String content,
    UUID channelId,
    UUID authorId
) {

}
