package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelRepository {
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
//            channels.add(data);
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
    public void addChannel(Channel channel) {
        channels.add(channel);
        save(file, channels);
    }

    @Override
    public void updateChannel(UUID channelId, String channelName) {
        Channel ch = channels.stream().filter(ch1 -> ch1.getId().equals(channelId)).findFirst().get();

        if (ch != null) {
            System.out.println(ch.getChannelName() + " -> " + channelName);
            ch.update(channelName);
            save(file, channels);
        } else if (channelName != null && !channelName.isEmpty()) {
            System.out.println("방이 없거나 입력이 잘못 되었습니다.");
        }

    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel ch = channels.stream()
                .filter(u -> u.getId().equals(channelId))
                .findFirst().get();

        if (ch != null) {
            System.out.println(ch.getChannelName() + "채널를 삭제했습니다.");
            channels.remove(ch);
            save(file, channels);
        } else {
            System.out.println("없거나 삭제된 채널입니다.");
        }

    }

    @Override
    public List<Channel> search(String name) {
        List<Channel> ch = channels.stream()
                .filter(u -> u.getChannelName().equals(name))
                .collect(Collectors.toList());

        if (ch.isEmpty()) {
            return null;
        } else {
            return ch;
        }
    }

    @Override
    public void searchChannel(String name) {
        List<Channel> ch = search(name);

        if (ch != null) {
            ch.stream().forEach(u -> System.out.println(u.getChannelName() + "방이 존재합니다."));
        } else {
            System.out.println("없거나 삭제 된 방입니다.");
        }

    }

    @Override
    public void searchChannelS(List<String> names) {

        names.forEach(name -> searchChannel(name));
    }


    @Override
    public void searchUpdateChannel() {
        AtomicBoolean notNull = new AtomicBoolean(false);

        channels.forEach(u -> {
            if (u.getCreated() != u.getUpdated()) {
                System.out.println("업데이트된 방: " + u.getChannelName());
                notNull.set(true);
            }
        });

        if (notNull.get() == false) {
            System.out.println("업데이트된 방이 없습니다.");
        }
    }

    @Override
    public List<Channel> getChannelList() {
        return channels;
    }
}
