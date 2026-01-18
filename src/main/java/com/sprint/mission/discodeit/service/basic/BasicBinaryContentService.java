package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage storage;

  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        (long) request.bytes().length,
        request.contentType()
    );

    storage.put(binaryContent.getId(), request.bytes());
    binaryContentRepository.save(binaryContent);
    return binaryContent;
  }

  @Override
  public BinaryContent find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContent with id " + binaryContentId + " not found"));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .toList();
  }

  @Override
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NoSuchElementException("BinaryContent with id "
          + binaryContentId + " not found");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }
}
