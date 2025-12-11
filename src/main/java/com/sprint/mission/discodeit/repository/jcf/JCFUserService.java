package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {
    final List<User> users = new ArrayList<>();

    // 사람 추가
    @Override
    public User create(User user) {
        users.add(user);
        return user;
    }

    // 사람 수정
    @Override
    public User update(UUID userid, String username) {
        User user = users.stream().filter(c -> c.getId().equals(userid)).findFirst().get();

        if (user != null) {
            user.update(username);
            return user;
        } else return null;
    }

    // 사람 삭제
    @Override
    public void delete(UUID userId) {
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst().get();

        if (user != null) {
            users.remove(user);
        }
    }

    // 사람 찾아서 객체 넘기기
    @Override
    public List<User> searchByName(List<String> name) {
        List<User> result = users.stream()
                .filter(user ->
                        name.stream().anyMatch(na -> user.getUserName().contains(na))
                ).toList();

        return result.isEmpty() ? null : result;
    }

    // 사람 찾기
    @Override
    public User findById(UUID id) {
        Optional<User> user = users.stream()
                .filter(c -> c.getId().equals(id)).findFirst();

        return user.orElse(null);
    }

    // 유저 리스트 넘기는거 만들기
    // 채널 만들 때 필요
    public List<User> getUsers() {
        return this.users;
    }
}