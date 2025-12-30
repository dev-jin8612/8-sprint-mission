package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserReqeust;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
    UserReqeust find(UUID userId);
    UserReqeust findByUsername(String username);
    List<UserReqeust> findAll();
    User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
    void delete(UUID userId);
}
