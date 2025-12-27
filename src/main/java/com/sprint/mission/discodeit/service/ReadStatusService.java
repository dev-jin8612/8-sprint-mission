package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest request);
    ReadStatus find(UUID readStatusId);
    ReadStatus findAllByUserId(UUID userId);
    ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request);
    void delete(UUID readStatusId);
}
