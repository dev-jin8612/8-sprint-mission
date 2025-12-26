package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public record MessageBinaryContentReqeust(
        String fileName,
        byte[] profileImg,
        String contents,
        Channel ch,
        UUID userId
) {
}
