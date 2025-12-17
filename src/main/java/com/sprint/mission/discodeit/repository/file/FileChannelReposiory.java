package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.ch.FindDTO;
import com.sprint.mission.discodeit.entity.ChType;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
public class FileChannelReposiory extends SaveLoadHelper implements ChannelRepository {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path file = Paths.get(String.valueOf(directory), "ch.ser");
    private final Map<UUID, Channel> channels;

    public FileChannelReposiory() {
        init(directory);
        channels = load(file);
    }

    @Override
    public Channel create(Channel channel) {
        channels.put(channel.getId(), channel);
        save(file, channels);
        return channel;
    }

    @Override
    public Channel update(UUID channelId, String channelName, ChType type) {
        Channel channel = Optional.ofNullable(channels.get(channelId))
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        channel.update(channelName, type);
        save(file, channels);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {

        channels.remove(channelId);
        save(file, channels);
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
    public Map<UUID, Channel> getChannelList() {
        return Optional.ofNullable(channels)
                .orElse(null);
    }
}
