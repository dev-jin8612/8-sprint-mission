package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "jcf"
)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private   Map<UUID, BinaryContent> bc;

    public JCFBinaryContentRepository() {
        this.bc = new HashMap<>();
    }

    @Override
    public BinaryContent create(BinaryContent binaryContent) {
        bc.put(binaryContent.getId(),binaryContent);
        return binaryContent;
    }

    @Override
    public void delete(UUID id) {
        bc.remove(id);
    }

    @Override
    public BinaryContent find(UUID id) {
        return null;
    }

    @Override
    public Map<UUID, BinaryContent> findAllByIdIn() {
        return Map.of();
    }
}
