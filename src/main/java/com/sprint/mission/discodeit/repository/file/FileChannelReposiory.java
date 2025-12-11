package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelReposiory extends SaveLoadHelper implements ChannelRepository {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path file = Paths.get(String.valueOf(directory), "ch.ser");
    private List<Channel> channels;

    public FileChannelReposiory() {
        init(directory);
        channels = loadFile(file);
    }

    public List<Channel> loadFile(Path directory) {
        Optional<List<Channel>> result = super.load(directory);
        return result.orElseGet(ArrayList::new);
    }


    @Override
    public Channel create(Channel channel) {
        channels.add(channel);
        save(file, channels);
        return channel;
    }

    @Override
    public Channel update(UUID channelId, String channelName) {
        Channel ch = channels.stream().filter(ch1 -> ch1.getId().equals(channelId)).findFirst().get();

        if (ch != null) {
            ch.update(channelName);
            save(file, channels);
            return ch;
        } else return null;
    }

    @Override
    public void delete(UUID channelId) {
        Channel ch = channels.stream()
                .filter(u -> u.getId().equals(channelId))
                .findFirst().get();

        if (ch != null) {
            channels.remove(ch);
            save(file, channels);
        }
    }

    @Override
    public List<Channel> searchByName(List<String> name) {
        List<Channel>ch = channels.stream()
                .filter(cha ->
                        name.stream().anyMatch(na -> cha.getChannelName().contains(na))
                ).toList();

        return ch.isEmpty()? null : ch;
    }

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
