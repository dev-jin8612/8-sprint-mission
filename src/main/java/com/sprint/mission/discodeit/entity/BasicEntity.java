package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BasicEntity {
    UUID id;
    Instant created;
    Instant updated;

    public BasicEntity() {
        this.id = UUID.randomUUID();
        this.created = Instant.now();
        this.updated = this.created;
    }
}
