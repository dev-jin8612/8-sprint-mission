package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
public class Channel extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 방이름
    private String name;
    // 참가자 명단
    private Set<UUID> users;
    // 타입
    private ChType type;

    // 생성자
    public Channel(String name, List<UUID> users, String type) {
        super();
        this.users = new HashSet<>(users);
        this.name = name;
        this.type = ChType.valueOf(type);
    }

    // 수정
    public void update(String name, String type) {
        boolean check = false;

        if (name != null && name.isEmpty() == false) {
            this.name = name;
            check = true;
        }

        if (type != null) {
            try {
                this.type = ChType.valueOf(type);
                check = true;
            } catch (IllegalArgumentException e) {
                // 뭔가 실패 했다는 메세지 나와야 할거 같은데
                check = false;
            }
        }

        if (check == false) {
            this.updated = Instant.now();
        }
    }

}
