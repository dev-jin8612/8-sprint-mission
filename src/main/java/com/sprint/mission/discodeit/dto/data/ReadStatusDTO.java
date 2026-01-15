package com.sprint.mission.discodeit.dto.data;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadStatusDTO(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID userId,
    UUID channelId
) {

}
