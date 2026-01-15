package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserDTO ToDto(User user);

  User toEntity(UserDTO userDTO);
}
