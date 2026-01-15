package com.sprint.mission.discodeit.entity;

import lombok.Getter;


import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {

    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //
    private UUID userId;
    private UUID channelId;
    private LocalDateTime lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, LocalDateTime lastReadAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        //
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant newLastReadAt) {
        boolean anyValueUpdated = false;
        if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
