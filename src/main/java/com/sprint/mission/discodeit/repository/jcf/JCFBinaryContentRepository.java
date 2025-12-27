package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

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
