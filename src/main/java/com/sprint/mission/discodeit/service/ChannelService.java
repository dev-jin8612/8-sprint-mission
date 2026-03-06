package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDTO create(PublicChannelCreateRequest request);

  ChannelDTO create(PrivateChannelCreateRequest request);

  ChannelDTO find(UUID channelId);

  List<ChannelDTO> findAllByUserId(UUID userId);

  ChannelDTO update(UUID channelId, PublicChannelUpdateRequest request);

  void delete(UUID channelId);
}