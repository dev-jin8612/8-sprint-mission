package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;

public record UserReqeust(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
}
