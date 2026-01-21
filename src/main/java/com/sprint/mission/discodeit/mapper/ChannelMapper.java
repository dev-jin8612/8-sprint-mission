package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.time.LocalDateTime;
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

  @Mapping(target = "online", expression = "java(user.getUserStatus() != null && user.getUserStatus().isOnline())")
  UserDTO mapUser(User user);

  default List<UUID> getParticipantIds(Channel channel) {
    if (channel.getType() == ChannelType.PUBLIC) {
      return Collections.emptyList();
    }

    return channel.getReadStatuses().stream()
        .map(ReadStatus::getUser)
        .map(this::mapUser)
        .map(userDTO -> userDTO.id())
        .toList();
  }

  default LocalDateTime getLastMessageAt(Channel channel) {
    return channel.getMessages().stream()
        .map(Message::getCreatedAt)
        .max(LocalDateTime::compareTo)
        .orElse(null);
  }
}
