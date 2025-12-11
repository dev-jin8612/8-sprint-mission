package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageReposiory extends SaveLoadHelper implements MessageRepository {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path filepath = Paths.get(String.valueOf(directory), "meg.ser");
    private List<Message> messages;

    public FileMessageReposiory() {
        init(directory);
        messages = loadFile(filepath);
    }

    public List<Message> loadFile(Path directory) {
        Optional<List<Message>> result = super.load(directory);
        return result.orElseGet(ArrayList::new);
    }

    // 메세지 추가
    @Override
    public Message create(Message message) {
        messages.add(message);
        save(filepath, messages);

        return message;
    }

    // 메세지 수정
    @Override
    public Message update(UUID mesUid, String contents) {
        Message m = messages.stream().filter(ch1 -> ch1.getId().equals(mesUid)).findFirst().get();

        if (m != null) {
            m.update(contents);
            save(filepath, messages);
            return m;
        }
        else return null;
    }

    // 메세지 삭제
    @Override
    public void delete(UUID mesUId) {
        Message m = messages.stream()
                .filter(u -> u.getId().equals(mesUId))
                .findFirst().get();

        if (m != null) {
            messages.remove(m);
            save(filepath, messages);
        }
    }

    // 메세지 찾아서 단일객체 넘기기
    @Override
    public List<Message> searchByContent(List<String> contents) {
        List<Message> meg = messages.stream()
                .filter(m ->
                        contents.stream().anyMatch(txt -> m.getMeg().contains(txt))
                ).toList();

        return meg.isEmpty() ? null : meg;
    }

    @Override
    public Message findById(UUID id) {
        Optional<Message> m = messages.stream()
                .filter(u -> u.getId().equals(id)).findFirst();

        return m.orElse(null);
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }
}
