package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserStatusReqeust(
        UUID userid,
        String name,
        String password,
        String email,
        UUID profileImg) {}

