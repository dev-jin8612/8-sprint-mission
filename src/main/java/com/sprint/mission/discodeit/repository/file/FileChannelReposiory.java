package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    public Channel update(UUID channelId, String channelName, List<UUID> usersId) {
        Channel channel = Optional.ofNullable(channels.get(channelId))
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        channel.update(channelName);
        save(file, channels);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!channels.containsKey(channelId)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }

        channels.remove(channelId);
        save(file, channels);
    }

    @Override
    public List<Channel> searchByName(List<String> name) {
        List<Channel> ch = channels.values().stream()
                .filter(cha ->
                        name.stream().anyMatch(na -> cha.getChannelName().contains(na))
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
