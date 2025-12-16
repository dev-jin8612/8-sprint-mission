package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserReposiory;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userService;

    @Override
    public User create(String name, String password, String email,UUID profileId) {
        User user = null;

        if (name != null && name.isEmpty() == false) {
            if (password != null && password.isEmpty() == false) {
                if (email != null && email.isEmpty() == false) {
                    user = new User(name, password, email,profileId);
                    return userService.create(user);
                }
            }
        }

        return Optional.ofNullable(user)
                .orElseThrow(() -> new NoSuchElementException("잘못된 형식입니다."));
    }

    @Override
    public User update(UUID userid, String name, String password, String email) {
        return userService.update(userid, name, password, email);
    }

    @Override
    public void delete(UUID id) {
        userService.delete(id);
    }

    @Override
    public List<User> searchByName(List<String> name) {
        return userService.searchByName(name);
    }

    @Override
    public User findById(UUID id) {
        return userService.findById(id);
    }

    @Override
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
