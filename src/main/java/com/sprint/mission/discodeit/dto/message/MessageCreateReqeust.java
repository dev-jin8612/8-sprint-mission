package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageCreateReqeust(
        String m,
        UUID userId,
        UUID chId,
        UUID attchmentId
        ) {
}
