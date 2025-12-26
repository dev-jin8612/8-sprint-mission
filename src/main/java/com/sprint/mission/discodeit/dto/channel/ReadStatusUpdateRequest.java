package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;

public record ReadStatusUpdateRequest(
        Instant newLastReadAt
) {
}
