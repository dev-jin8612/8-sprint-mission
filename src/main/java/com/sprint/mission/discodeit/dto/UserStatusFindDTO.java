package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserStatusFindDTO(
        UUID id,
        String username,
        String email,
        UUID profile,
        boolean login
) {

}
