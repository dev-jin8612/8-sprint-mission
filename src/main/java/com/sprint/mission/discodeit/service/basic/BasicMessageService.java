package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageRepository {
    private final MessageRepository messageService;

    public BasicMessageService(MessageRepository messageService) {
        this.messageService = messageService;
    }

    @Override
    public void addMessage(Message message) {
        messageService.addMessage(message);
    }

    @Override
    public void updateMessage(UUID mesUid, String contents) {
        messageService.updateMessage(mesUid, contents);
    }

    @Override
    public void deleteMessage(UUID mesUid) {
        messageService.deleteMessage(mesUid);
    }

    @Override
    public List<Message> search(String content) {
        return messageService.search(content);
    }

    @Override
    public void searchMessage(String content) {
        messageService.searchMessage(content);
    }

    @Override
    public void searchMessageS(List<String> contents) {
        messageService.searchMessageS(contents);
    }

    @Override
    public void searchUpdateMessage() {
        messageService.searchUpdateMessage();
    }
}
