package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id; //메세지 번호
    private String meg; //메세지 내용
    private UUID sender; //보낸 사람
    private UUID roomId; // 보낸 방번호

    private long created;
    private long updated;

//    나중에는 보낸 방 위치도 알게 roomId 알게 수정하기
    public Message(String meg,UUID userId) {
        this.id = UUID.randomUUID();
        this.meg = meg;
        this.sender = this.id;
        this.created = System.currentTimeMillis();
        this.updated = System.currentTimeMillis();
    }

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

    // 수정 필요
//    public void update(UUID id,String meg) {
    public void update(String meg) {
//        나중에는 userId랑 비교해서 본인이 보낸게 맞는지 확인하고 수정하게 만들기
        this.meg = meg ;
        this.updated = System.currentTimeMillis();
    }
}
