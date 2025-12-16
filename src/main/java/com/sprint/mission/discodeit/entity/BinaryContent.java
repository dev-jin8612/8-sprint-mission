package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryContent {
    private UUID id;
    private UUID userId;
    private UUID profileId;
    private UUID megId;
    private UUID megfile;

    public BinaryContent(UUID id, UUID userId, UUID megId, UUID megfile, UUID profileId) {
        this.id = id;
        this.userId = userId;
        this.profileId = profileId;
        this.megId = megId;
        this.megfile = megfile;
    }
}
