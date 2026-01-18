package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
  MessageDTO toDTO(Message message);
  Message toEntity(MessageDTO messageDTO);

  // Entity → DTO
  default UUID map(BinaryContent binaryContent) {
    return binaryContent == null ? null : binaryContent.getId();
  }

  // DTO → Entity
  default BinaryContent map(UUID id) {
    if (id == null) return null;

    BinaryContent binaryContent = new BinaryContent();
    binaryContent.setId(id); // id만 세팅 (프록시 역할)
    return binaryContent;
  }
}
