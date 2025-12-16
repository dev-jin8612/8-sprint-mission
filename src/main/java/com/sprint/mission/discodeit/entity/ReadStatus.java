package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BasicEntity {
    private UUID chId;
    private UUID userId;

    public ReadStatus(UUID chId, UUID userId) {
        super();
        this.chId = chId;
        this.userId = userId;
    }

    public void update(){
        this.updated = Instant.now();
    }
}
