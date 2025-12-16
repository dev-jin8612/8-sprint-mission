package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageService;

    @Override
    public Message create(String meg, Channel ch, UUID userId) {
        Message m = null;

        if (ch.getUsers().stream().anyMatch(u -> u.equals(userId))) {
            if (meg != null && meg.trim().length() > 0) {
                m = new Message(meg, userId, ch.getId());
                System.out.println("메세지가 생성 됐습니다.");
                return messageService.create(m);
            }
        }

        return Optional.ofNullable(m)
                .orElseThrow(() -> new NoSuchElementException("잘못된 형식입니다."));
    }

    @Override
    public Message update(UUID mesUid, String contents) {
        return messageService.update(mesUid, contents);
    }

    @Override
    public void delete(UUID id) {
        messageService.delete(id);
    }

    @Override
    public List<Message> searchByContent(List<String> contents) {
        return messageService.searchByContent(contents);
    }

    @Override
    public Message findById(UUID id) {
        return messageService.findById(id);
    }

    @Override
    public List<Message> getMessages() {
        return messageService.getMessages();
    }
}
