package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.BinaryCreateDTO;
import lombok.Getter;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Getter
public class BinaryContent {
    private final UUID id;
    // 선택적 프로필 대체할려면 이미지를 여러개 받아야 겠지?
    private Map<UUID, File> profileImg;
    // UUID에 megid 넣고 file에 해당 메세지로 보낸
    // 파일을 넣으면 되지 않을까?
    private Map<UUID, File> megfile;

    public BinaryContent(BinaryCreateDTO dto) {
        this.id = UUID.randomUUID();
        this.profileImg = dto.profileImg();
        this.megfile = dto.megfile();
    }
}
