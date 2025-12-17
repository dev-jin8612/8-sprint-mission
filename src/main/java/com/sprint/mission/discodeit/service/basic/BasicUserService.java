package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.UserStatusFindDTO;
import com.sprint.mission.discodeit.dto.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userService;
    private final UserStatusRepository userStatus;
    private final BinaryContentRepository bcRepository;

    @Override
    public UserStatusCreateDTO create(UserStatusCreateDTO createDTO) {
        userService.getUsers().values().forEach(
                user -> {
                    if (user.getName().equals(createDTO.name())) {
                        throw new IllegalArgumentException("이미 존재합니다.");
                    } else if (user.getEmail().equals(createDTO.email())) {
                        throw new IllegalArgumentException("이미 존재합니다.");
                    }
                }
        );

        User u = new User(createDTO);
        UserStatus us = new UserStatus(u.getId());

        userStatus.create(us);
        userService.create(u);
        // 이거 근데 실제 이미지랑 다른게 좀 필요할거 같은데
//        bcRepository.create(new BinaryContent());

        // 생성 말고 다른 dto? 유저 정보만 있는걸로? 그러면 vo가 낮지 않나?
        return createDTO;
    }

    @Override
    public UserStatusUpdateDTO update(UserStatusUpdateDTO updateDTO) {
        if (userService.findById(updateDTO.userid()) == null) {
            throw new IllegalArgumentException("유저가 없습니다.");
        }

        userService.update(updateDTO);

        // 다른 dto 반환하게 만들어야 하나?
        // 유저 정보만 있는걸로? 그러면 vo가 낫지 않나?
        return updateDTO;
    }

    @Override
    public void delete(UUID id) {
        if (userService.findById(id) == null) {
            throw new IllegalArgumentException("이미 삭제 되었습니다.");
        }

        userStatus.delete(id);
        bcRepository.delete(id);
        userService.delete(id);
    }

    @Override
    public List<User> searchByName(List<String> name) {
        return userService.searchByName(name);
    }

    @Override
    public UserStatusFindDTO findById(UUID id) {
        // 온라인 상태 포함해서 내보네기
        UserStatus us = userStatus.findById(id);
        User u = userService.findById(id);

        if (u != null) {
            return new UserStatusFindDTO(u.getId(), u.getName(), u.getEmail(), u.getProfile(), us.checkLogin());
        }

        return null;
    }

    @Override
    public Map<UUID, User> getUsers() {
        return userService.getUsers();
    }
}
