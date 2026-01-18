package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReadStatusMapper {
  ReadStatusMapper INSTANCE = Mappers.getMapper(ReadStatusMapper.class);
  ReadStatusDTO toDTO(ReadStatus readStatus);
  ReadStatus toEntity(ReadStatusDTO readStatusDTO);

}
