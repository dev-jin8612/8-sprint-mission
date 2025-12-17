package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.BinaryCreateDTO;
import lombok.Getter;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Getter
public class BinaryContent {
    private UUID id;
    private UUID profileImg;
    // UUID에 megid 넣고 file에 해당 메세지로 보낸
    // 파일을 넣으면 되지 않을까?
    private Map<UUID, File> megfile;

    public BinaryContent(BinaryCreateDTO dto) {
        this.id = UUID.randomUUID();
        this.profileImg = dto.profileImg();
        this.megfile = dto.megfile();
    }


}
