package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.ch.ChUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "jcf"
)
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
    public Channel update(ChUpdateDTO dto) {
        Channel channel = Optional.ofNullable(channels.get(dto.chId()))
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        channel.update(dto.name(),dto.type());
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
    public Map<UUID,Channel>  getChannelList() {
        return Optional.ofNullable(channels)
                .orElse(null);
    }
}