package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.UserStatusFindDTO;
import com.sprint.mission.discodeit.dto.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface UserService {
    // 등록
    UserStatusCreateDTO create(UserStatusCreateDTO createDTO);

    // 정보 수정
    UserStatusUpdateDTO update(UserStatusUpdateDTO updateDTO);

    // 삭제
    void delete(UUID id);

    // 이름으로 찾기
    List<User> searchByName(List<String> name);

    // id로 찾기
    UserStatusFindDTO findById(UUID id);

    Map<UUID,User> getUsers();
}
