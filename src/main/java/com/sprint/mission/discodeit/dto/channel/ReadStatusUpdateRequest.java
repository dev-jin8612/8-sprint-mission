package com.sprint.mission.discodeit.dto.channel;

import java.time.LocalDateTime;

public record ReadStatusUpdateRequest(
    LocalDateTime newLastReadAt
) {

}
