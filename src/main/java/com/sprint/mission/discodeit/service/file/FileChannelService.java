package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileChannelService implements ChannelService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path file = Paths.get(String.valueOf(directory), "ch.ser");
    private final Map<UUID, Channel> channels;

    public FileChannelService() {
        init(directory);
        channels = load(file);
    }

    public void init(Path directory) {
        // 저장할 경로의 파일 초기화
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> void save(Path directory, T data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directory.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Map<UUID,Channel> load(Path directory) {
        if (Files.exists(directory)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(directory.toFile()))) {
                Object data = ois.readObject();
                return (Map<UUID,Channel>) data;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new HashMap<UUID,Channel>();
        }
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

        channel.updateChName(channelName);
        channel.updateChUsers(usersId);
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
