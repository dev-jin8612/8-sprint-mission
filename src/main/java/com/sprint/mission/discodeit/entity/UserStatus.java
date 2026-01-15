package com.sprint.mission.discodeit.entity;

import lombok.Getter;


import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus {

    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //
    private UUID userId;
    private LocalDateTime lastActiveAt;

    public UserStatus(UUID userId, LocalDateTime lastActiveAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        //
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public void update(Instant lastActiveAt) {
        boolean anyValueUpdated = false;
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public Boolean isOnline() {
        LocalDateTime instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

        return lastActiveAt.isAfter(instantFiveMinutesAgo);
    }
}
