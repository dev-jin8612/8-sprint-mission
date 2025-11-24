package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id; //메세지 번호
    private String meg; //메세지 내용
    private String sender; //보낸 사람

    private long created;
    private long updated;
}
