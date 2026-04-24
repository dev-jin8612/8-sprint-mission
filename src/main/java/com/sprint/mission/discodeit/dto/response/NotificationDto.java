package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        Instant createAt,
        UUID receiverId,
        String title,
        String content
) {
}
