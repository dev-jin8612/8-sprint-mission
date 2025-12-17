package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserStatusFindDTO(
        UUID id,
        String username,
        String email,
        UUID profile,
        boolean login
) {

}
