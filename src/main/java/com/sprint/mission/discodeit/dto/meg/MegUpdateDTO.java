package com.sprint.mission.discodeit.dto.meg;

import java.util.UUID;

public record MegUpdateDTO(
        UUID mesUid,
        String contents
) {
}
