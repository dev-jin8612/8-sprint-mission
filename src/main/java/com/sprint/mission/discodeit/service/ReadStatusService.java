package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDTO create(ReadStatusCreateRequest request);

  ReadStatusDTO find(UUID readStatusId);

  List<ReadStatusDTO> findAllByUserId(UUID userId);

  ReadStatusDTO update(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID readStatusId);
}
