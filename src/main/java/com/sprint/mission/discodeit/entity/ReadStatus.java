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
    private Instant lastReadAt;

    public ReadStatus(UUID chId, UUID userId, Instant lastReadAt) {
        super();
        this.chId = chId;
        this.userId = userId;
        this.lastReadAt = lastReadAt;
    }

    public Instant update() {
        this.updated = Instant.now();
        return updated;
    }
}
