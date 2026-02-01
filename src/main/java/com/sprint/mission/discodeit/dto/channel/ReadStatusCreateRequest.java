package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
}
