package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDTO create(UserCreateRequest userCreateRequest,
      MultipartFile profileCreateRequest);

  UserDTO find(UUID userId);

  List<UserDTO> findAll();

  UserDTO update(UUID userId, UserUpdateRequest userUpdateRequest,
      MultipartFile profileCreateRequest);

  void delete(UUID userId);
}
