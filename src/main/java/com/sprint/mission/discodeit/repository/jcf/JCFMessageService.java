package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageRepository {
    final List<Message> messages = new ArrayList<Message>();

    // 메세지 추가
    @Override
    public Message create(Message message) {
        messages.add(message); return message;
    }

    // 메세지 수정
    @Override
    public Message update(UUID mesUid, String contents) {
        Message m = messages.stream().filter(ch1 -> ch1.getId().equals(mesUid)).findFirst().get();

        if (m != null) {
            m.update(contents);
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
        }
    }

    // 메세지 찾아서 단일객체 넘기기
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