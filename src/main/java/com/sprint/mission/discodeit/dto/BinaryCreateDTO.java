package com.sprint.mission.discodeit.dto;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public record BinaryCreateDTO(
        UUID profileImg,
        Map<UUID, File> megfile
) {}
