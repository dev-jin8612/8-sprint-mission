package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    //메세지 번호
    private UUID id;
    //메세지 내용
    private String meg;
    //보낸 사람
    private UUID sender;
    // 보낸 방번호
    private UUID roomId;
    // 생성 시간
    private long created;
    // 수정 시간
    private long updated;

    public Message(String m, UUID userId, UUID chId) {
        id = UUID.randomUUID();
        meg = m;
        sender = userId;
        roomId = chId;
        long now = System.currentTimeMillis();
        created = now;
        updated = now;
    }

    // getter
    public UUID getRoomId() {
        return roomId;
    }

    public UUID getId() {
        return id;
    }

    public String getMeg() {
        return meg;
    }

    public UUID getSender() {
        return sender;
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }

    // 수정
    public void update(String m) {
        meg = m;
        updated = System.currentTimeMillis();
    }
}
