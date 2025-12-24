package com.sprint.mission.discodeit.dto.meg;

import com.sprint.mission.discodeit.entity.Channel;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public record MessageBinaryContentReqeust(
        Map<UUID, File> profileImg,
        Map<UUID, File> megfile,
        String contents,
        Channel ch,
        UUID userId
) {
}
