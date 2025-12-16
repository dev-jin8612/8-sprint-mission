package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> users;

    public JCFUserService() {
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
    public User update(UUID userid,String name,String password,String email) {
        User user = Optional.ofNullable(users.get(userid))
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        user.update(name,password,email);
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
                        name.stream().anyMatch(na -> user.getName().contains(na))
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

    // 유저 리스트 넘기는거 만들기
    // 채널 만들 때 필요
    @Override
    public List<User> getUsers() {
        List<User> user = new ArrayList<>(users.values());

        return Optional.ofNullable(user)
                .orElse(null);
    }
}