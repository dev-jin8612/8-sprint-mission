package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileBinaryContentReposiory extends SaveLoadHelper implements BinaryContentRepository {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path file = Paths.get(String.valueOf(directory), "bc.ser");
    private final Map<UUID, BinaryContent> bc;

    public FileBinaryContentReposiory() {
        init(directory);
        bc = load(file);
    }

    @Override
    public BinaryContent create(BinaryContent binaryContent) {
        bc.put(binaryContent.getId(),binaryContent);
        save(file, binaryContent);
        return binaryContent;
    }

    @Override
    public void delete(UUID id) {
        bc.remove(id);
    }

    @Override
    public BinaryContent findById(UUID id) {
        return null;
    }

    @Override
    public Map<UUID, BinaryContent> findAll() {
        return Map.of();
    }
}
