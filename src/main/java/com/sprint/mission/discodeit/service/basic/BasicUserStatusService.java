package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest request) {
        UUID userId = request.userId();

        if (userRepository.findById(userId)==null) {
            throw new NoSuchElementException("User with id " + userId + " does not exist");
        }
        if (userStatusRepository.find(userId)!=null) {
            throw new IllegalArgumentException("UserStatus with id " + userId + " already exists");
        }

//        Instant lastActiveAt = request.lastActiveAt();
//        UserStatus userStatus = new UserStatus(userId, lastActiveAt);
        UserStatus userStatus = new UserStatus(userId);
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
