package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository extends SaveLoadHelper implements ReadStatusRepository {
    private final Path directory;
    private final Path file;
    private Map<UUID, ReadStatus> read;

    public FileReadStatusRepository(
            @Value("${discodeit.repository.file-directory}") String dir
    ) {
        this.directory = Paths.get(dir);
        this.file = directory.resolve("read.ser");
        init(directory);
        read = load(file);
    }

    @Override
    public ReadStatus create(ReadStatus dto) {
        read.put(dto.getChId(), dto);
        save(file, read);
        return dto;
    }

    @Override
    public Instant update(UUID chid) {
        Instant i = read.get(chid).update();
        save(file, read);
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
