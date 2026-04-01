package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
@RequiredArgsConstructor
public abstract class UserMapper {
    protected SessionRegistry sessionRegistry;

    @Mapping(target = "online", expression = "java(checkOnlineStatus(user))")
    public abstract UserDto toDto(User user);

    protected Boolean checkOnlineStatus(User user) {
        return sessionRegistry.getAllPrincipals().stream()
                .anyMatch(principal -> {
                    String sessionUsername = "";

                    if (principal instanceof UserDetails) {
                        sessionUsername = ((UserDetails) principal).getUsername();
                    } else if (principal instanceof String) {
                        sessionUsername = (String) principal;
                    }

                    return user.getUsername().equals(sessionUsername) &&
                            !sessionRegistry.getAllSessions(principal, false)
                                    .isEmpty();
                });
    }
}