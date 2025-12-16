package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileMessageService implements MessageService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path filepath = Paths.get(String.valueOf(directory), "meg.ser");
    private final Map<UUID, Message> messages;

    public FileMessageService() {
        init(directory);
        messages = load(filepath);
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

    public Map<UUID,Message> load(Path directory) {
        if (Files.exists(directory)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(directory.toFile()))) {
                Object data = ois.readObject();
                return (Map<UUID,Message>) data;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new HashMap<UUID,Message>();
        }
    }

    // 메세지 추가
    @Override
    public Message create(Message m) {
        messages.put(m.getId(), m);
        save(filepath, messages);

        return m;
    }

    // 메세지 수정
    @Override
    public Message update(UUID mesUid, String contents) {
        Message m = Optional.ofNullable(messages.get(mesUid))
                .orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));

        m.update(contents);
        save(filepath, messages);

        return m;
    }

    // 메세지 삭제
    @Override
    public void delete(UUID mesUId) {
        if (!messages.containsKey(mesUId)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }

        save(filepath, messages);
        messages.remove(mesUId);
    }

    // 메세지 찾아서 단일객체 넘기기
    @Override
    public List<Message> searchByContent(List<String> contents) {
        List<Message> meg = messages.values().stream()
                .filter(m ->
                        contents.stream().anyMatch(txt -> m.getMeg().contains(txt))
                ).toList();

        return Optional.ofNullable(meg)
                .orElse(null);
    }

    @Override
    public Message findById(UUID id) {
        return Optional.ofNullable(messages.get(id))
                .orElse(null);
    }

    @Override
    public List<Message> getMessages() {
        List<Message> meg = new ArrayList<>(messages.values());
        return Optional.ofNullable(meg)
                .orElse(null);
    }
}
