package com.sprint.mission.discodeit.entity;

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

    // 생성자
    public User(String name, String password, String email, UUID profile) {
        super();
        this.name = name;
        this.password = password;
        this.email = email;
        // 일단 랜덤으로 자리만 잡기
        this.profile=profile;
    }

    // 수정
    public void update(String name,String password,String email) {
        boolean check =false;

        if (name != null && name.isEmpty() == false) {
            this.name = name;
            check = true;
        }

        if (password != null && password.isEmpty() == false) {
            this.password = password;
            check = true;
        }

        if (email != null && email.isEmpty() == false) {
            this.email = email;
            check = true;
        }

        if (check == false) {this.updated = Instant.now();}
    }
}
