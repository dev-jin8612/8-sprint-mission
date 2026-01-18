package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChannelMapper {
  ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);
  ChannelDTO toDTO(Channel channel);
  Channel toEntity(ChannelDTO channelDTO);
}
