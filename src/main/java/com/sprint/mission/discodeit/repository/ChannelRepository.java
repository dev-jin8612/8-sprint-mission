package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    // 채널
    // 등록
    void addChannel(Channel channel);

    // 정보 수정
    void updateChannel(UUID channelId, String channelname);

    // 삭제
    void deleteChannel(UUID channelId);

    // 방 찾기
    List<Channel> search(String name);

    // 방찾기
    void searchChannel(String name);

    // 여러 방 찾기
    void searchChannelS(List<String> channelIds );

    // 수정된 방 찾기
    void searchUpdateChannel();

    // 방 정보 넘기기
    List<Channel> getChannelList();
}
