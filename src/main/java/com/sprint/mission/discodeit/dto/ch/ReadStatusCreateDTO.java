package com.sprint.mission.discodeit.dto.ch;

import com.sprint.mission.discodeit.entity.ChType;

import java.util.List;
import java.util.UUID;

public record ReadStatusCreateDTO(
String name,
List<UUID> memberIds
) {
}
