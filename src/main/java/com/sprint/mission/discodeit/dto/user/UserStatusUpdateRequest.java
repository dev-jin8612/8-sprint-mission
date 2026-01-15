package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.time.LocalDateTime;

public record UserStatusUpdateRequest(
        LocalDateTime newLastActiveAt
) {
}
