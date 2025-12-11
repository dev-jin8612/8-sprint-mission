package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // 등록
    Channel create(Channel channel);

    // 정보 수정
    Channel update(UUID channelId, String channelname, List<UUID> usersIds);

    // 삭제
    void delete(UUID channelId);

    // 방 이름으로 찾기
    List<Channel> searchByName(List<String> name);

    // 방 id로 찾기
    Channel findById(UUID id);

    // 방 정보 넘기기
    List<Channel> getChannelList();
}
