package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record FindReqeust(
        UUID chId,
        Set<UUID> users,
        String name,
        ChannelType type,
        Instant lastMeg
) {
}
