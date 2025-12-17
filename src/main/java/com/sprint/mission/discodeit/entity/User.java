package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.UserStatusUpdateDTO;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

//유저 정보는 간략하게 비번 등을 생략
@Getter
public class User extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 이름
    private String name;
    // 비번
    private String password;
    // 메일
    private String email;
    // 프로필 이미지
    private UUID profile;
    // 현 프로필
    private UUID profileImg;

    // 생성자
    public User(UserStatusCreateDTO createDTO) {
        super();
        this.name = createDTO.name();
        this.password = createDTO.password();
        this.email = createDTO.email();
        // 일단 랜덤으로 자리만 잡기
        this.profile=createDTO.profileId();
    }

    // 수정
    public void update(UserStatusUpdateDTO updateDTO) {
        boolean check =false;

        if (updateDTO.name() != null && updateDTO.name().isEmpty() == false) {
            this.name = updateDTO.name();
            check = true;
        }

        if (updateDTO.password() != null && updateDTO.password().isEmpty() == false) {
            this.password = updateDTO.password();
            check = true;
        }

        if (updateDTO.email() != null && updateDTO.email().isEmpty() == false) {
            this.email = updateDTO.email();
            check = true;
        }

        if (updateDTO.profileImg() != null) {
            this.profileImg = updateDTO.profileImg();
            check = true;
        }

        if (check == false) {this.updated = Instant.now();}
    }
}
