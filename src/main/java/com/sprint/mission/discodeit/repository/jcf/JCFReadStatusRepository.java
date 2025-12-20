package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class JCFReadStatusRepository implements ReadStatusRepository {
    private   Map<UUID, ReadStatus> read;

    public JCFReadStatusRepository() {
        this.read = new HashMap<>();
    }

    @Override
    public ReadStatus create(ReadStatus dto) {
        read.put(dto.getChId(), dto);
        return dto;
    }

    @Override
    public Instant update(UUID chid) {
        Instant i= read.get(chid).update();
        return i;
    }

    @Override
    public void delete(UUID chId) {
        read.remove(chId);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return Optional.ofNullable(read.get(id))
                .orElse(null);
    }

    @Override
    public Map<UUID, ReadStatus> findAll() {
        return Optional.ofNullable(read)
                .orElse(null);
    }
}
