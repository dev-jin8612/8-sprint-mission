package com.sprint.mission.discodeit.dto.meg;

import java.util.UUID;

public record MessageUpdateReqeust(
        UUID mesUid,
        String contents
) {
}
