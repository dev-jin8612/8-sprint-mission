package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.ch.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileReadStatusReposiory extends SaveLoadHelper implements ReadStatusRepository {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path file = Paths.get(String.valueOf(directory), "read.ser");
    private final Map<UUID, ReadStatus> read;

    public FileReadStatusReposiory() {
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
        return read.get(chid).update();
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
