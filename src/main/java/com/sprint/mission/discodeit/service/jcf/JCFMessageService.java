package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messages;

    public JCFMessageService() {
        this.messages = new HashMap<>();
    }

    // 메세지 추가
    @Override
    public Message create(String contents, Channel ch, UUID userId) {
        Message m = null;

        if (ch.getUsers().stream().anyMatch(u -> u.equals(userId))) {
            m = new Message(contents, userId, ch.getId());
            messages.put(m.getId(), m);
            System.out.println("메세지가 생성 됐습니다.");
        }

        return Optional.ofNullable(m)
                .orElseThrow(() -> new NoSuchElementException("잘못된 형식입니다."));
    }

    // 메세지 수정
    @Override
    public Message update(UUID mesUid, String contents) {
        Message m = Optional.ofNullable(messages.get(mesUid))
                .orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));

        m.update(contents);
        return m;
    }

    // 메세지 삭제
    @Override
    public void delete(UUID mesUId) {
        if (!messages.containsKey(mesUId)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }
        messages.remove(mesUId);
    }

    // 메세지 찾아서 단일객체 넘기기
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

    // 메세지들
    @Override
    public List<Message> getMessages() {
        List<Message> meg = new ArrayList<>(messages.values());
        return Optional.ofNullable(meg)
                .orElse(null);
    }
}