package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.BinaryCreateReqeust;
import lombok.Getter;

import java.io.Serializable;
import java.io.File;
import java.util.Map;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String fileName;
    private final String contentType;
    private byte[] profileImg;

    public BinaryContent(String fileName, String contentType, byte[] profileImg) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.contentType = contentType;
        this.profileImg = profileImg;
    }
}
