package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageUpdateReqeust(
        UUID mesUid,
        String contents
) {
}
