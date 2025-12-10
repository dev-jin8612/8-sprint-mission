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

    public Message(String meg, UUID userId, Channel ch) {
        // 심화 내용
        // anyMatch는 자동완성으로 나왔는데 일치하는게 있나 확인하는 것
        // 이걸로 메세지를 보내는 사람이 보내는 채팅방에 있는 유저인지를 확인
        if (ch.getUsers().stream().anyMatch(u -> u.equals(userId))) {
            this.id = UUID.randomUUID();
            this.meg = meg;
            this.sender = userId;
            this.roomId = ch.getId();
            long now = System.currentTimeMillis();
            this.created = now;
            this.updated = now;

            System.out.println("메세지가 생성되었습니다.");
        }else{
            // 조건 안맞으면 강제오류 내기
            throw new RuntimeException("생성 불가");
        }
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
    public void update(String meg) {
        this.meg = meg;
        this.updated = System.currentTimeMillis();
    }
}
