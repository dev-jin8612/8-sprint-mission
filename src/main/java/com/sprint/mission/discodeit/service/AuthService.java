package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.LoginSerachDTO;
import com.sprint.mission.discodeit.dto.user.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserStatusFindDTO;
import com.sprint.mission.discodeit.dto.user.UserStatusDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface AuthService {
    // 등록
    UserStatusDTO create(UserStatusCreateDTO createDTO);

    // 정보 수정
    UserStatusDTO update(UserStatusDTO updateDTO);

    // 삭제
    void delete(UUID id);

    // 이름으로 찾기
    List<User> searchByName(List<String> name);

    // id로 찾기
    UserStatusFindDTO findById(UUID id);

    Map<UUID,User> getUsers();

    // 로그인
    UserStatusFindDTO login(LoginSerachDTO dto);
}
