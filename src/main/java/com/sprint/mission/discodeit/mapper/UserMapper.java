package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {
    @Mapping(target = "online", expression = "java(checkOnlineStatus(user))")
    public abstract UserDto toDto(User user);

//    TODO: 여기 원래 세션에 유저 있으면 온라인 표시 만들던건데 이제 세션을 안쓰니 다른걸 찾아야 하는데
}