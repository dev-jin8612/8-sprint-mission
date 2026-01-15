package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
  MessageDTO toDTO(Message message);
  Message toEntity(MessageDTO messageDTO);
}
