package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {

}
