package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public interface UserService {
    // 등록
    User create(String name,String password,String email,UUID profileId);

    // 정보 수정
    User update(UUID userid,String name,String password,String email);

    // 삭제
    void delete(UUID id);

    // 이름으로 찾기
    List<User> searchByName(List<String> name);

    // id로 찾기
    User findById(UUID id);

    List<User> getUsers();
}
