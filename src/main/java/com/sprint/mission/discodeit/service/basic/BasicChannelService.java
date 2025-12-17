package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ch.ChUpdateDTO;
import com.sprint.mission.discodeit.dto.ch.FindDTO;
import com.sprint.mission.discodeit.dto.ch.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.entity.ChType;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository chService;
    private final ReadStatusRepository readRespository;

    @Override
    public Channel createPrivate(List<UUID> memberIds) {
        Channel channel = new Channel("", memberIds, ChType.PRIVATE);

        for (UUID memberId : memberIds) {
            readRespository.create(new ReadStatus(channel.getId(), memberId));
        }

        return chService.create(channel);
    }

    @Override
    public Channel createPublic(ReadStatusCreateDTO dto) {
        Channel channel = new Channel(dto.name(), dto.memberIds(), ChType.PUBLIC);

        for (UUID memberId : dto.memberIds()) {
            readRespository.create(new ReadStatus(channel.getId(), memberId));
        }

        return chService.create(channel);
    }

    @Override
    public Channel update(ChUpdateDTO dto) {
        if (dto.type() == ChType.PUBLIC) {
            return chService.update(dto.chId(), dto.name(), dto.type());
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> clist = chService.getChannelList();

        if (!clist.containsKey(id)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }

        // 해당 채팅방의 메세지도 지우게 만들기
        readRespository.delete(id);
        chService.delete(id);
    }

    @Override
    public List<Channel> searchByName(List<String> name) {
        return chService.searchByName(name);
    }

    @Override
    public FindDTO findById(UUID id) {
        Channel ch = chService.findById(id);
        ReadStatus read = readRespository.findById(id);

        if (ch == null) {
            return null;
        }else if (ch.getType() == ChType.PRIVATE) {
            return new FindDTO(ch.getId(), ch.getUsers(), ch.getName(), ch.getType(), read.getUpdated());
        } else {
            return new FindDTO(ch.getId(), null, ch.getName(), ch.getType(), read.getUpdated());
        }
    }

    @Override
    public Map<UUID, Channel> getChannelList() {
        return chService.getChannelList();
    }
}
