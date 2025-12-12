package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;


import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> users;

    public JCFUserRepository() {
        this.users = new HashMap<>();
    }

    // 사람 추가
    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    // 사람 수정
    @Override
    public User update(UUID userid, String username) {
        User user = Optional.ofNullable(users.get(userid))
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        user.update(username);
        return user;
    }

    // 사람 삭제
    @Override
    public void delete(UUID userId) {
        if (!users.containsKey(userId)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }
        users.remove(userId);
    }

    // 사람 찾아서 객체 넘기기
    @Override
    public List<User> searchByName(List<String> name) {
        List<User> result = users.values().stream()
                .filter(user ->
                        name.stream().anyMatch(na -> user.getUserName().contains(na))
                ).toList();

        return Optional.ofNullable(result)
                .orElse(null);
    }

    // 사람 찾기
    @Override
    public User findById(UUID id) {
        return Optional.ofNullable(users.get(id))
                .orElse(null);
    }

    // 유저 리스트 채널 만들 때 필요
    public List<User> getUsers() {
        List<User> user = new ArrayList<>(users.values());

        return Optional.ofNullable(user)
                .orElse(null);
    }
}