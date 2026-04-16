package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jwt.JwtRegistry; // SessionRegistry 대신 JwtRegistry 사용
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {
    @Autowired
    protected JwtRegistry jwtRegistry;

    @Mapping(target = "online", expression = "java(checkOnlineStatus(user))")
    public abstract UserDto toDto(User user);

    protected Boolean checkOnlineStatus(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }

        return jwtRegistry.hasActiveJwtInformationByUserId(user.getId());
    }
}