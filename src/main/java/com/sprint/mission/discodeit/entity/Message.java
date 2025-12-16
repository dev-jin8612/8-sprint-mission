package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
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

    public Message(String m, UUID userId, UUID chId) {
        super();
        meg = m;
        sender = userId;
        roomId = chId;
    }

    // 수정
    public void update(String m) {
        if (meg != null && meg.trim().length() > 0) {
            meg = m;
            updated = Instant.now();
        }
    }
}
