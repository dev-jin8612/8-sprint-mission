package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels;

    public JCFChannelRepository() {
        this.channels = new HashMap<>();
    }

    @Override
    public Channel create(Channel channel) {
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel update(UUID channelId, String channelName, String type) {
        Channel channel = Optional.ofNullable(channels.get(channelId))
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        channel.update(channelName,type);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!channels.containsKey(channelId)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }

        channels.remove(channelId);
    }

    @Override
    public List<Channel> searchByName(List<String> name) {
        List<Channel> ch = channels.values().stream()
                .filter(cha ->
                        name.stream().anyMatch(na -> cha.getName().contains(na))
                ).toList();

        return Optional.ofNullable(ch)
                .orElse(null);
    }

    @Override
    public Channel findById(UUID id) {
        return Optional.ofNullable(channels.get(id))
                .orElse(null);
    }

    @Override
    public List<Channel> getChannelList() {
        List<Channel> ch = new ArrayList<>(channels.values());

        return Optional.ofNullable(ch)
                .orElse(null);
    }
}