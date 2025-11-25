package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.MessageService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    // 방 번호
    private UUID id;
    // 방이름, 없다면 기본 이름
    private String name = "defaultName";
    // 참가자 명단
    private List<User> users;
    // 생성 시간
    private long created;
    // 수정 시간
    private long updated;

    // 생성자
    public Channel(String name, List<User> users) {
        this.id = UUID.randomUUID();
        this.users = users;
        this.created = System.currentTimeMillis();
        this.updated = System.currentTimeMillis();

        // 찾아보니 spring5.3이상에서는 ObjectUtils로 null,blank 확인 가능
        // ex) ObjectUtils.isEmpty(String name);
        //이름이 공백이거나 없는지 확인, 없다면 기본이름이 될것임
        if (name != null && name.isEmpty()==false) {
            this.name = name;
        }
    }

    // getter
    public String getChannelName() {
        return name;
    }
    public UUID getId() {
        return id;
    }
    public List<User> getUsers() {
        return users;
    }
    public long getCreated() {
        return created;
    }
    public long getUpdated() {
        return updated;
    }

    // 수정, 방이름
    public void update(String name) {
        this.name = name;
        this.updated = System.currentTimeMillis();
    }

    // 수정, 방참가자
    public void update(List<User> users) {
        this.users = users;
        this.updated = System.currentTimeMillis();
    }
}
