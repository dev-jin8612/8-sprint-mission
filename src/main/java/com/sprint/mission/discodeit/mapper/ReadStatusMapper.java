package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  @Mapping(target = "userId", source = "user")
  @Mapping(target = "channelId", source = "channel")
  ReadStatusDTO toDto(ReadStatus readStatus);

  default UUID map(User user) {
    return user == null ? null : user.getId();
  }

  default UUID map(Channel channel) {
    return channel == null ? null : channel.getId();
  }

  List<ReadStatusDTO> toDtoList(List<ReadStatus> readStatuses);
}
