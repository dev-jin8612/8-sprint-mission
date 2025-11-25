package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class User {
    private UUID id; //유저 id
    private String name="defaultName"; //이름

    private long created;
    private long updated;

//    getter
    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return name;
    }

    public long getCreated() {
        return created;
    }

    public long getUpdated() {
        return updated;
    }

    // 생성자
    public User(String name) {
        this.name = name;
        this.id =  UUID.randomUUID();
        this.created = System.currentTimeMillis();
        this.updated = System.currentTimeMillis();
    }


    // method
    public void update(String name) {
        this.name = name;
        this.updated = System.currentTimeMillis();
    }
}
