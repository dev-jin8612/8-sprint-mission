package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID userId;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
    }

    public Instant update() {
        this.updated = Instant.now();
        return updated;
    }

    public boolean checkLogin() {
        long diffSeconds = Instant.now().getEpochSecond() - updated.getEpochSecond();
        if (diffSeconds < 300) {
            return true;
        } else {
            return false;
        }
    }
}
