package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDTO create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  MessageDTO find(UUID messageId);

  PageResponse<MessageDTO> findAllByChannelId(UUID channelId, Instant createdAt, Pageable pageable);

  MessageDTO update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
