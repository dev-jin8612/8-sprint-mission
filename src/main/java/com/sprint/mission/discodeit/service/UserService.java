package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserStatusCreateReqeust;
import com.sprint.mission.discodeit.dto.user.UserStatusReqeust;
import com.sprint.mission.discodeit.dto.user.UserStatusFindReqeust;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface UserService {
    // 등록
    UserStatusReqeust create(UserStatusCreateReqeust createDTO);

    // 정보 수정
    UserStatusReqeust update(UserStatusReqeust updateDTO);

    // 삭제
    void delete(UUID id);

    // 이름으로 찾기
    List<User> searchByName(List<String> name);

    // id로 찾기
    UserStatusFindReqeust findById(UUID id);

    Map<UUID,User> getUsers();
}
