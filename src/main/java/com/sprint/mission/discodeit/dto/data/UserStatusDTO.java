package com.sprint.mission.discodeit.dto.data;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserStatusDTO(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID userId,
    LocalDateTime lastActiveAt
) {

}
