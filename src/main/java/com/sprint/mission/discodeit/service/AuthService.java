package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;


public interface AuthService {
    // 등록
    User login(LoginRequest dto);
}
