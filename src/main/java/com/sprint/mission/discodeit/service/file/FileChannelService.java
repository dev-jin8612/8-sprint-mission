package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path file = Paths.get(String.valueOf(directory), "ch.ser");
    private List<Channel> channels;

    public FileChannelService() {
        init(directory);
        channels = load(file);
    }

    public static void init(Path directory) {
        // 저장할 경로의 파일 초기화
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> void save(Path directory, T data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directory.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Channel> load(Path directory) {
        if (Files.exists(directory)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(directory.toFile()))) {
                Object data = ois.readObject();
                return (List<Channel>) data;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
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
