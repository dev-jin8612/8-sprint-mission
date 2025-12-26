package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.message.MessageUpdateReqeust;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileMessageRepository extends SaveLoadHelper implements MessageRepository {
    private final Path directory;
    private final Path filepath;
    private Map<UUID, Message> messages;

    public FileMessageRepository(
            @Value("${discodeit.repository.file-directory}") String dir
    ) {
        this.directory = Paths.get(dir);
        this.filepath = directory.resolve("meg.ser");
        init(directory);
        messages = load(filepath);
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
    public Message update(MessageUpdateReqeust dto) {
        Message m = Optional.ofNullable(messages.get(dto.mesUid()))
                .orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));

        m.update(dto.contents());
        save(filepath, messages);

        return m;
    }

    // 메세지 삭제
    @Override
    public void delete(UUID mesUId) {
        if (!messages.containsKey(mesUId)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }

        messages.remove(mesUId);
        save(filepath, messages);
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
