package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    // 등록
    UserStatus create(UserStatus dto);

    // 로그인 시간 수정
    Instant update(UUID id);

    // ID로 업데이트
    Instant updateByUserId(UUID id);

    // 삭제, 유저 삭제시 같이
    void delete(UUID userid);

    Optional<UserStatus> find(UUID id);

    Map<UUID, UserStatus> findAll();
}
