package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id; // 방 번호
    private String channelName; // 방이름
    private List<User> users; //참가자 명단
    private List<Message> messages; //메세지 내용

    private long created;
    private long updated;
}
