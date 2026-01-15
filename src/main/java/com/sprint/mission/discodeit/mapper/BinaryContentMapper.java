package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BinaryContentMapper {
  BinaryContentMapper INSTANCE = Mappers.getMapper(BinaryContentMapper.class);
  BinaryContentDTO toDTO(BinaryContent binaryContent);
  BinaryContent toEntity(BinaryContentDTO binaryContentDTO);
}
