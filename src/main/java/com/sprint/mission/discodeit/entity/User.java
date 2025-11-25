package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

//유저 정보는 간략하게 비번 등을 생략
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    //유저 id
    private UUID id;
    //이름, 입력이 없다면 기본 이름
    private String name = "defaultName";
    // 생성 시간
    private long created;
    // 수정 시간
    private long updated;

    // getter
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
        this.id = UUID.randomUUID();
        this.created = System.currentTimeMillis();
        this.updated = System.currentTimeMillis();

        // 찾아보니 spring5.3이상에서는 ObjectUtils로 null,blank 확인 가능
        // ex) ObjectUtils.isEmpty(String name);
        //이름이 공백이거나 없는지 확인, 없다면 기본이름이 될것임
        if (name != null && name.isEmpty() == false) {
            this.name = name;
        }
    }

    // 수정, 비번은 없으니 간략하게
    public void update(String name) {
        this.name = name;
        this.updated = System.currentTimeMillis();
    }
}
