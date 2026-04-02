package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    UserDTO getCurrentUserInfo(UserDetails userDetails);

    UserDTO updateRole(UserRoleUpdateRequest userRoleUpdateRequest);
}
