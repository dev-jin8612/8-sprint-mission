package com.sprint.mission.discodeit.dto.ch;

import com.sprint.mission.discodeit.entity.ChType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record FindDTO(
        UUID chId,
        Set<UUID> users,
        String name,
        ChType type,
        Instant lastMeg
) {
}
