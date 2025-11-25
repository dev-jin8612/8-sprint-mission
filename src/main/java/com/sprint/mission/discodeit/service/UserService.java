package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface UserService {
    // 유저등록
    void addUser(User user);

    // 유저 정보 수정
    void updateUser(UUID userId, String username);

    // 유저 삭제
    void deleteUser(UUID userId);

    // 객체 주는걸로 바꾸기
    // 유저 찾기
//    User searchUser(UUID userId);
    List<User> search(String name);

    void searchUser(String name);

    //객체 주는걸로 바꾸기
    // 유저 여러명 찾기
    void searchUserS(List<String> names );

    // 수정된 유저 찾기
    void searchUpdateUser();

    // 수정된 유저들 찾기
//    void searchUpdateUserS(List<String> names);


    // 유저 리스트 넘기는거 만들기
    // 채널 만들 때 필요
    List<User> getUsers();
}
