package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    // 등록
    Channel create(Channel ch);

    // 정보 수정
    Channel update(UUID channelId, String channelname, List<UUID> usersIds);

    // 삭제
    void delete(UUID id);

    // 이름으로 찾기
    List<Channel> searchByName(List<String> name);

    // 찾기는 리턴이 없을 수 있으니 나중에 optional해주기
    // id로 찾기
    Channel findById(UUID id);

    List<Channel> getChannelList();
}
