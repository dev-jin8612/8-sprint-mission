package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDTO create(BinaryContentCreateRequest request);

  BinaryContentDTO find(UUID binaryContentId);

  List<BinaryContentDTO> findAllByIdIn(List<UUID> binaryContentIds);

  void delete(UUID binaryContentId);
}
