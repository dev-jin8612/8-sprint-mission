package com.sprint.mission.discodeit.dto.data;

import java.time.LocalDateTime;
import java.util.UUID;

public record BinaryContentDTO(
    UUID id,
    LocalDateTime createdAt,
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
    ) {

}
