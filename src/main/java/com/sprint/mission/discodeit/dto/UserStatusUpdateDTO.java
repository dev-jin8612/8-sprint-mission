package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserStatusUpdateDTO(
        UUID userid,
        String name,
        String password,
        String email,
        UUID profileId) {}

