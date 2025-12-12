package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository chService;

    public BasicChannelService(ChannelRepository chService) {
        this.chService = chService;
    }

    @Override
    public Channel create(Channel ch) {
        return chService.create(ch);
    }

    @Override
    public Channel update(UUID channelId, String channelName) {
        return chService.update(channelId, channelName);
    }

    @Override
    public void delete(UUID id) {
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
