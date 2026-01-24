package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
  @Mapping(target = "participantIds", expression = "java(getParticipantIds(channel))")
  @Mapping(target = "lastMessageAt", expression = "java(getLastMessageAt(channel))")
  ChannelDTO toDto(Channel channel);

  default List<UUID> getParticipantIds(Channel channel) {
    if (channel.getType() == ChannelType.PUBLIC) {
      return Collections.emptyList();
    }

    return channel.getReadStatuses().stream()
        .map(readStatus->readStatus.getId())
        .toList();
  }

  default Instant getLastMessageAt(Channel channel) {
    return channel.getMessages().stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo)
        .orElse(null);
  }
}
