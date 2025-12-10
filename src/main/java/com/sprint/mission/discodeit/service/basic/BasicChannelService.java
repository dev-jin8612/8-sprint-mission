//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//
//import java.util.List;
//import java.util.UUID;
//
//public class BasicChannelService implements ChannelRepository {
//    private final ChannelRepository chService;
//
//    public BasicChannelService(ChannelRepository chService) {
//        this.chService = chService;
//    }
//
//    @Override
//    public void addChannel(Channel channel) {
//        chService.addChannel(channel);
//
//    }
//
//    @Override
//    public void updateChannel(UUID channelId, String channelname) {
//        chService.updateChannel(channelId, channelname);
//    }
//
//    @Override
//    public void deleteChannel(UUID channelId) {
//        chService.deleteChannel(channelId);
//    }
//
//    @Override
//    public List<Channel> search(String name) {
//        return chService.search(name);
//    }
//
//    @Override
//    public void searchChannel(String name) {
//        chService.searchChannel(name);
//    }
//
//    @Override
//    public void searchChannelS(List<String> channelIds) {
//        chService.searchChannelS(channelIds);
//    }
//
//    @Override
//    public void searchUpdateChannel() {
//        chService.searchUpdateChannel();
//    }
//
//    @Override
//    public List<Channel> getChannelList() {
//        return chService.getChannelList();
//    }
//}
