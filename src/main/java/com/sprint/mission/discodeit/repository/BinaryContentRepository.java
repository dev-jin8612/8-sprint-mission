package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    // 등록
    BinaryContent create(BinaryContent binaryContent);

    // 삭제, 유저 삭제시 같이
    void delete(UUID id);

    Optional<BinaryContent> find(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> id);
}
