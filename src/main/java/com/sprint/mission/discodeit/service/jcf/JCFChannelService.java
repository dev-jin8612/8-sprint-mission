package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channels;

    public JCFChannelService() {
        this.channels = new HashMap<>();
    }

    // 채널 추가
    @Override
    public Channel create(Channel channel) {
        channels.put(channel.getId(), channel);
        return channel;
    }

    // 채널 수정
    @Override
    public Channel update(UUID channelId, String channelName, List<UUID> usersIds) {
        Channel channel = Optional.ofNullable(channels.get(channelId))
                        .orElseThrow(()-> new NoSuchElementException("채널이 없습니다."));

        channel.update(channelName);
        return channel;
    }

    // 채널 삭제
    @Override
    public void delete(UUID channelId) {
        if(!channels.containsKey(channelId)){
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }

        channels.remove(channelId);
    }

    // 찾기, 이름 검색
    @Override
    public List<Channel> searchByName(List<String> name) {
        List<Channel> ch = channels.values().stream()
                .filter(cha ->
                        name.stream().anyMatch(na -> cha.getChannelName().contains(na))
                ).toList();

        return Optional.ofNullable(ch)
                .orElse(null);
    }

        // 채널 찾기
    @Override
    public Channel findById(UUID id) {
        return Optional.ofNullable(channels.get(id))
                .orElse(null);
    }

    // 참여자
    @Override
    public List<Channel> getChannelList() {
        List<Channel> ch= new ArrayList<>(channels.values());

        return Optional.ofNullable(ch)
                .orElse(null);
    }
}