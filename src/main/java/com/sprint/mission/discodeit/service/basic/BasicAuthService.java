package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
// TODO 생각해보니 세션 없앴으니 여기도 수정해야되네
public class BasicAuthService implements AuthService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUserInfo(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserDto userDto = userMapper.toDto(user);
        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateRole(UserRoleUpdateRequest userRoleUpdateRequest) {
        User user = userRepository.findById(userRoleUpdateRequest.userId())
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.updateRole(userRoleUpdateRequest.newRole());
        User updatedUser = userRepository.save(user);
        invalidateUserSessions(updatedUser.getUsername());

        UserDto userDto = userMapper.toDto(updatedUser);
        return userDto;
    }

    private void invalidateUserSessions(String username) {
        try {
            List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

            for (Object principal : allPrincipals) {
                UserDetails userDetails = (UserDetails) principal;
                String principalName = userDetails.getUsername();

                if (username.equals(principalName)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);

                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            log.error("[UserService] 세션 무효화 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
