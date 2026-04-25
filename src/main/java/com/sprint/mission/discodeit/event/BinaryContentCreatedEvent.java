package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;

public record BinaryContentCreatedEvent(
        byte[] bytes,
        Instant createAt,
        BinaryContent binaryContent
) {
}