package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.*;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    // 방 번호
    private UUID id;
    // 방이름, 없다면 기본 이름
    private String name = "defaultName";
    // 참가자 명단
    private Set<UUID> users;
    // 생성 시간
    private long created;
    // 수정 시간
    private long updated;

    // 생성자
    public Channel(String name, List<UUID> users) {
        this.id = UUID.randomUUID();
        this.users = new HashSet<>(users);
        long now = System.currentTimeMillis();
        this.created = now;
        this.updated = now;

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
    public List<UUID> getUsers() {
        return users.stream().toList();
    }
    public long getCreated() {
        return created;
    }
    public long getUpdated() {
        return updated;
    }

    // 수정, 방이름
    public void updateChName(String name) {
        this.name = name;
        this.updated = System.currentTimeMillis();
    }

    // 수정, 방참가자
    public void updateChUsers(List<UUID> users) {
        this.users = new HashSet<>(users);
        this.updated = System.currentTimeMillis();
    }
}
