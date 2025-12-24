package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.meg.MessageCreateReqeust;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //메세지 내용
    private String meg;
    //보낸 사람
    private UUID sender;
    // 보낸 방번호
    private UUID roomId;
    // Binary
    List<UUID> attchmentIds;

//    public Message(String m, UUID userId, UUID chId, UUID attchmentId) {
    public Message(MessageCreateReqeust dto) {
        super();
        meg = dto.m();
        sender = dto.userId();
        roomId = dto.chId();

        attchmentIds = new ArrayList<>();
        attchmentIds.add(dto.attchmentId());
    }

    // 수정
    public void update(String m) {
        if (meg != null && meg.trim().length() > 0) {
            meg = m;
            updated = Instant.now();
        }
    }
}
