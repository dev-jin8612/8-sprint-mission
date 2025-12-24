package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.user.UserStatusReqeust;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    // 등록
    User create(User user);

    // 정보 수정
    User update(UserStatusReqeust updateDTO);

    // 삭제
    void delete(UUID id);

    // 이름으로 찾기
    List<User> searchByName(List<String> name);

    // id로 찾기
    User findById(UUID id);

    Map<UUID,User> getUsers();
}
