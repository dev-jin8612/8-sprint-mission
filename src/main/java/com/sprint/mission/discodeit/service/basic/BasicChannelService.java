package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository chService;

    @Override
    public Channel create(String name, List<UUID> memberIds, String type) {
        Channel channel = new Channel(name, memberIds, type);
        return chService.create(channel);
    }

    @Override
    public Channel update(UUID channelId, String channelName, String type) {
        return chService.update(channelId, channelName, type);
    }

    @Override
    public void delete(UUID id) {
        List<Channel> clist = chService.getChannelList();

        if (!clist.containsKey(channelId)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }
        chService.delete(id);
    }

    @Override
    public List<Channel> searchByName(List<String> name) {
        return chService.searchByName(name);
    }

    @Override
    public Channel findById(UUID id) {
        return chService.findById(id);
    }

    @Override
    public List<Channel> getChannelList() {
        return chService.getChannelList();
    }
}
