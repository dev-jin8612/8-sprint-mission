package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BasicEntity {
    private UUID userId;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
    }

    public void update() {
        this.updated = Instant.now();
    }

    public void checkLogin() {
        long diffSeconds = Instant.now().getEpochSecond() - updated.getEpochSecond();
        if (diffSeconds < 300) {
            System.out.println("로그인 중입니다.");
        }
    }
}
