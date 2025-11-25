package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id; // 방 번호
    private String channelName; // 방이름
    private List<User> users; //참가자 명단
    private List<Message> messages; //메세지 내용
    // 나중에 메세지 넣고 메세지 추가도 진행하게 만들기

    private long created;
    private long updated;

    public Channel(String name, List<User> users) {
        this.channelName = name;
        this.id = UUID.randomUUID();
        this.users = users;
        this.created = System.currentTimeMillis();
        this.updated = System.currentTimeMillis();
    }

    public String getChannelName() {
        return channelName;
    }

    public UUID getId() {
        return id;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }

    // 수정 필요
    public void update(String name) {
        this.channelName = name;
        this.updated = System.currentTimeMillis();
    }
}
