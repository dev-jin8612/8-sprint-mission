package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.meg.MessageBinaryContentReqeust;
import com.sprint.mission.discodeit.dto.meg.MessageCreateReqeust;
import com.sprint.mission.discodeit.dto.meg.MessageUpdateReqeust;
import com.sprint.mission.discodeit.dto.user.BinaryCreateReqeust;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository bcRepository;

    @Override
    public Message create(MessageBinaryContentReqeust dto) {
        Message m = null;

        if (dto.ch().getUsers().stream().anyMatch(u -> u.equals(dto.userId()))) {
            if (dto.contents() != null && dto.contents().trim().length() > 0) {

                BinaryCreateReqeust bcDto = new BinaryCreateReqeust(dto.profileImg(), dto.megfile(),null);
                BinaryContent bc = new BinaryContent(bcDto);
                bcRepository.create(bc);

                MessageCreateReqeust mc = new MessageCreateReqeust(dto.contents(),dto.userId(),dto.ch().getId(),bc.getId());
                m = new Message(mc);

                messageRepository.create(m);
                System.out.println("메세지가 생성 됐습니다.");
            }
        }

        return m;
    }

    @Override
    public Message update(MessageUpdateReqeust dto) {
        return messageRepository.update(dto);
    }

    @Override
    public void delete(UUID id) {
        //binary 삭제 추가하기
        messageRepository.delete(id);
    }

    @Override
    public List<Message> searchByContent(List<String> contents) {
        return messageRepository.searchByContent(contents);
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> getMessages() {
        return messageRepository.getMessages();
    }
}
