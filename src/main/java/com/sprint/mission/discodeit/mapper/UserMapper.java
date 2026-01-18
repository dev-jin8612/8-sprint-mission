package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
  UserDTO ToDto(User user);
  User toEntity(UserDTO userDTO);


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
