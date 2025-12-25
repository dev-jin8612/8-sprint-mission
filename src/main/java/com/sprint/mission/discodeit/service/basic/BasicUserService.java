package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginReqeust;
import com.sprint.mission.discodeit.dto.user.UserStatusCreateReqeust;
import com.sprint.mission.discodeit.dto.user.UserStatusReqeust;
import com.sprint.mission.discodeit.dto.user.UserStatusFindReqeust;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository UserStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserStatusReqeust create(UserStatusCreateReqeust createDTO) {
        List<User> users = userRepository.searchByName(List.of(createDTO.name()));

        if (!users.isEmpty()) {
            throw new IllegalArgumentException("이미 존재합니다.");
        }

        User u = new User(createDTO);
        UserStatus us = new UserStatus(u.getId());

        UserStatusReqeust usDto = new UserStatusReqeust(
                u.getId(),
                u.getName(),
                u.getPassword(),
                u.getEmail(),
                u.getProfileImg()
        );

        UserStatusRepository.create(us);
        userRepository.create(u);
        // 이거 근데 실제 이미지랑 다른게 좀 필요할거 같은데
//        binaryContentRepository.create(new BinaryContent());

        // 생성 말고 다른 dto? 유저 정보만 있는걸로? 그러면 vo가 낮지 않나?
//        return createDTO;
        return usDto;
    }

    @Override
    public UserStatusReqeust update(UserStatusReqeust updateDTO) {
        if (userRepository.findById(updateDTO.userid()) == null) {
            throw new IllegalArgumentException("유저가 없습니다.");
        }

        userRepository.update(updateDTO);

        // 다른 dto 반환하게 만들어야 하나?
        // 유저 정보만 있는걸로? 그러면 vo가 낫지 않나?
        return updateDTO;
    }

    @Override
    public void delete(UUID id) {
        if (userRepository.findById(id) == null) {
            throw new IllegalArgumentException("이미 삭제 되었습니다.");
        }

        UserStatusRepository.delete(id);
        binaryContentRepository.delete(id);
        userRepository.delete(id);
    }

    @Override
    public List<User> searchByName(List<String> name) {
        return userRepository.searchByName(name);
    }

    @Override
    public UserStatusFindReqeust findById(UUID id) {
        // 온라인 상태 포함해서 내보네기
        UserStatus us = UserStatusRepository.find(id);
        User u = userRepository.findById(id);

        if (u != null) {
            return new UserStatusFindReqeust(u.getId(), u.getName(), u.getEmail(), u.getProfile(), us.checkLogin());
        }

        return null;
    }

    @Override
    public Map<UUID, User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public UserStatusFindReqeust login(LoginReqeust dto) {
        for (User u : userRepository.getUsers().values()) {
            if (u.getName().equals(dto.name()) &&
                    u.getPassword().equals(dto.password())) {

                return new UserStatusFindReqeust(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getProfileImg(),
                        true
                );
            }
        }
        return null;
    }
}
