package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserStatusCreateReqeust;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateReqeust request) {
        Optional<User> user =userRepository.findByUsername(request.name());

        if (user==null) {
            throw new NoSuchElementException("User does not exist");
        }
        if (userStatusRepository.find(user.get().getId())!=null) {
            throw new IllegalArgumentException("UserStatus already exists");
        }

        UserStatus userStatus = new UserStatus(user.get().getId());
        return userStatusRepository.create(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return userStatusRepository.find(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList(userStatusRepository.findAll().keySet());
    }

    @Override
    public UserStatus update(UUID userStatusId) {

        UserStatus userStatus = userStatusRepository.find(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));

        userStatus.update();

        return userStatusRepository.create(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {

        UserStatus userStatus = userStatusRepository.find(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));

        userStatus.update();

        return userStatusRepository.create(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (userStatusRepository.find(userStatusId) == null) {
            throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
        }
        userStatusRepository.delete(userStatusId);
    }
}
