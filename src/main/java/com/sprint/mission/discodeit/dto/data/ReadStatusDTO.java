package com.sprint.mission.discodeit.dto.data;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadStatusDTO(
    UUID id,
    UUID userId,
    UUID channelId,
    LocalDateTime lastReadAt
) {

}
