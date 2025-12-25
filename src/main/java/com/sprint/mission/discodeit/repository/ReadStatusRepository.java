package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    // 등록
    ReadStatus create(ReadStatus dto);

    // 로그인 시간 수정
    Instant update(UUID id);

    // 삭제, 채널 삭제시 같이
    void delete(UUID chid);

    Optional<ReadStatus> findById(UUID id);

    Map<UUID, ReadStatus> findAll();
}
