package com.sprint.mission.discodeit.event;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentCreatedEvent(
        byte[] bytes,
        Instant createAt,
        UUID binaryContent
) {
}