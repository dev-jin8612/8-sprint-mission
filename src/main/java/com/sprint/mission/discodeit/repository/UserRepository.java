package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    // 등록
    User create(User user);

    // 정보 수정
    User update(UUID userid, String username);

    // 삭제
    void delete(UUID id);

    // 찾기는 리턴이 없을 수 있으니 나중에 optional해주기
    // optional 해주면서 구현된 메서드에서 sout 없애거나 줄이기
    // 이름으로 찾기
    List<User> searchByName(List<String> name);

    // id로 찾기
    User findById(UUID id);

    public List<User> getUsers();
}
