package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.meg.MegBCDTO;
import com.sprint.mission.discodeit.dto.meg.MegCreateDTO;
import com.sprint.mission.discodeit.dto.meg.MegUpdateDTO;
import com.sprint.mission.discodeit.dto.user.BinaryCreateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
    private final BinaryContentRepository bcRepository;

    @Override
    public Message create(MegBCDTO dto) {
        Message m = null;

        if (dto.ch().getUsers().stream().anyMatch(u -> u.equals(dto.userId()))) {
            if (dto.contents() != null && dto.contents().trim().length() > 0) {

                BinaryCreateDTO bcDto = new BinaryCreateDTO(dto.profileImg(), dto.megfile(),null);
                BinaryContent bc = new BinaryContent(bcDto);
                bcRepository.create(bc);

                MegCreateDTO mc = new MegCreateDTO(dto.contents(),dto.userId(),dto.ch().getId(),bc.getId());
                m = new Message(mc);

                messageService.create(m);
                System.out.println("메세지가 생성 됐습니다.");
            }
        }

        return m;
    }

    @Override
    public Message update(MegUpdateDTO dto) {
        return messageService.update(dto);
    }

    @Override
    public void delete(UUID id) {
        //binary 삭제 추가하기
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
