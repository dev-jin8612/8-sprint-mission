package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID chId;
    private final UUID userId;

    public ReadStatus(UUID chId, UUID userId) {
        super();
        this.chId = chId;
        this.userId = userId;
    }

    public Instant update() {
        this.updated = Instant.now();
        return updated;
    }
}
