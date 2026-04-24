package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record BinaryContentCreatedEvent(
        // TODO 흠... ID만 있어도 돼지 않을까?
        UUID binaryContentId,
        byte[] bytes
) { }