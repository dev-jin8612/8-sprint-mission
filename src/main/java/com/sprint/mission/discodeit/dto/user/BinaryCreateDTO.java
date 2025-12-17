package com.sprint.mission.discodeit.dto.user;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public record BinaryCreateDTO(
        Map<UUID, File> profileImg,
        Map<UUID, File> megfile
) {}
