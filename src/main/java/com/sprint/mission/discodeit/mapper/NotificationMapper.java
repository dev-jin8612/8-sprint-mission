package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.NotificationDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "receiverId", source = "user.id")
    NotificationDto ReadToNoteDto(ReadStatus readStatus);
}
