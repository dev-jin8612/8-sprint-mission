package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileUserStatusRepository extends SaveLoadHelper implements UserStatusRepository {
    private final Path directory;
    private final Path file;
    private Map<UUID, UserStatus> status;

    public FileUserStatusRepository(
            @Value("${discodeit.repository.file-directory}") String dir
    ) {
        this.directory = Paths.get(dir);
        this.file = directory.resolve("userStatus.ser");

        init(directory);
        status = load(file);
    }

    @Override
    public UserStatus create(UserStatus dto) {
        // userid로 찾기 변하게 만들기
        // 자체적으로 찾아서 쓸일 없을거 같아서
        status.put(dto.getUserId(), dto);
        save(file, status);
        return dto;
    }

    @Override
    //dto로 동작하게 수정
    public Instant update(UUID id) {
        Instant i = status.get(id).update();
        save(file, status);
        return Instant.now();
    }

    @Override
    public Instant updateByUserId(UUID id) {
        Instant i = status.get(id).update();
        save(file, status);
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
