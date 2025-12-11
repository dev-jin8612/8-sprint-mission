package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository; 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelRepository {
    final List<Channel> channels = new ArrayList<>();

    // 채널 추가
    @Override
    public Channel create(Channel channel) {
        channels.add(channel);return  channel;
    }

    // 채널 수정
    @Override
    public Channel update(UUID channelId, String channelName) {
        Channel ch = channels.stream().filter(ch1 -> ch1.getId().equals(channelId)).findFirst().get();

        if (ch != null) {
            ch.update(channelName);
            return ch;
        } else return null;
    }

    // 채널 삭제
    @Override
    public void delete(UUID channelId) {
        Channel ch = channels.stream()
                .filter(u -> u.getId().equals(channelId))
                .findFirst().get();

        if (ch != null) {
            channels.remove(ch);
        }
    }

    // 채널 찾아서 객체 넘기기
    @Override
    public List<Channel> searchByName(List<String> name) {
        List<Channel>ch = channels.stream()
                .filter(cha ->
                        name.stream().anyMatch(na -> cha.getChannelName().contains(na))
                ).toList();

        return ch.isEmpty()? null : ch;
    }

    // 채널 찾기
    @Override
    public Channel findById(UUID id) {
        Optional<Channel> ch = channels.stream()
                .filter(c -> c.getId().equals(id)).findFirst();

        return ch.orElse(null);
    }

    @Override
    public List<Channel> getChannelList() {
        return channels;
    }
}