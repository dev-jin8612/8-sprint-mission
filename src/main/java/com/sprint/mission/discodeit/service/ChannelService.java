package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateReqeust;
import com.sprint.mission.discodeit.dto.channel.FindReqeust;
import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateReqeust;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    // 등록
    Channel createPrivate(List<UUID> memberIds);
    Channel createPublic(ReadStatusCreateReqeust dto);

    // 정보 수정
    Channel update(ChannelUpdateReqeust dto);

    // 삭제
    void delete(UUID channelId);

    // 방 이름으로 찾기
    List<Channel> searchByName(List<String> name);

    // 방 id로 찾기
    FindReqeust findById(UUID id);

    // 방 정보 넘기기
    Map<UUID,Channel> getChannelList();
}
