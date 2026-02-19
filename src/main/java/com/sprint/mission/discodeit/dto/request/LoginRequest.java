package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record LoginRequest(
    @NotBlank String username,
    @NotBlank String password
) {
}
