//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//
//import java.util.List;
//import java.util.UUID;
//
//public class BasicMessageService implements MessageService {
//    private final MessageRepository messageService;
//
//    public BasicMessageService(MessageRepository messageService) {
//        this.messageService = messageService;
//    }
//
//    @Override
//    public Message create(Message meg) {
//        return messageService.create(meg);
//    }
//
//    @Override
//    public Message update(UUID mesUid, String contents) {
//        return messageService.update(mesUid, contents);
//    }
//
//    @Override
//    public void delete(UUID id) {
//        messageService.delete(id);
//    }
//
//    @Override
//    public List<Message> searchByContent(List<String> contents) {
//        return messageService.searchByContent(contents);
//    }
//
//    @Override
//    public Message findById(UUID id) {
//        return messageService.findById(id);
//    }
//
//    @Override
//    public List<Message> getMessages() {
//        return messageService.getMessages();
//    }
//}
