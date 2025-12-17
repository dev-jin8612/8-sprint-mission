package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserStatusUpdateDTO(
        UUID userid,
        String name,
        String password,
        String email,
        UUID profileImg) {}

