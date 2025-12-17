package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public interface ReadStatusRespository {
    // 등록
    ReadStatus create(ReadStatus readStatus);

    // 확인 시간 수정
    Instant update();

    // 삭제, 유저 삭제시 같이
    void delete(UUID id);

    ReadStatus findById(UUID id);

    Map<UUID, ReadStatus> findAll();
}
