package com.sprint.mission.discodeit.security.checker;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.user.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component("userAuthChecker")
public class UserAuthChecker {
    private final UserRepository userRepository;

    public boolean isOwner(Authentication authentication, UUID userId) {
        // 로그인 검증
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof DiscodeitUserDetails userDetails) {
            UserDto userDto = userDetails.getUserDto();
            return userDto.id().equals(userId);
        }
        return false;
    }
}