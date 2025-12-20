package com.sprint.mission.discodeit.dto.meg;

import java.util.UUID;

public record MegCreateDTO(
        String m,
        UUID userId,
        UUID chId,
        UUID attchmentId
        ) {
}
