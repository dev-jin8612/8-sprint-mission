package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserStatusMapper {
  UserStatusMapper INSTANCE = Mappers.getMapper(UserStatusMapper.class);
  UserStatusDTO toDTO(UserStatus userStatus);
  UserStatus toEntity(UserStatusDTO userStatusDTO);
}
