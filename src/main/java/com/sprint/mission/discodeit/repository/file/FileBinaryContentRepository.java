package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileBinaryContentRepository extends SaveLoadHelper implements BinaryContentRepository {
    private final Path directory;
    private final Path file;
    private Map<UUID, BinaryContent> bc;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory}") String dir) {
        this.directory = Paths.get(dir);
        this.file = directory.resolve("bc.ser");
        init(directory);
        bc = load(file);
    }

    @Override
    public BinaryContent create(BinaryContent binaryContent) {
        bc.put(binaryContent.getId(), binaryContent);
        save(file, bc);
        return binaryContent;
    }

    @Override
    public void delete(UUID id) {
        bc.remove(id);
    }

    @Override
    public Optional<BinaryContent> find(UUID id) {
        return Optional.ofNullable(bc.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        List<BinaryContent> bcList = new ArrayList<>();

        binaryContentIds.stream().map(uuid ->
                bcList.add(bc.get(uuid))
        );

        return bcList;
    }
}
