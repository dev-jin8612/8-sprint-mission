package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "jcf"
)
public class JCFUserStatusRepository implements UserStatusRepository {
    private Map<UUID, UserStatus> status;

    public JCFUserStatusRepository() {
        status = new HashMap<>();
    }

    @Override
    public UserStatus create(UserStatus dto) {
        // userid로 찾기 변하게 만들기
        // 자체적으로 찾아서 쓸일 없을거 같아서
        status.put(dto.getUserId(),dto);
        return dto;
    }

    @Override
    //dto로 동작하게 수정
    public Instant update(UUID id) {
        Instant i= status.get(id).update();
        return Instant.now();
    }

    @Override
    public Instant updateByUserId(UUID id) {
        Instant i= status.get(id).update();
        return Instant.now();
    }

    @Override
    public void delete(UUID userid) {
        status.remove(userid);
    }

    @Override
    public UserStatus find(UUID id) {
        return Optional.ofNullable(status.get(id))
                .orElse(null);
    }

    @Override
    public Map<UUID, UserStatus> findAll() {
        return Optional.ofNullable(status)
                .orElse(null);
    }
}
