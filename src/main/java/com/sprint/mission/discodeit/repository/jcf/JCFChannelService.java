package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    final List<Channel> channels = new ArrayList<>();

    // 채널 추가
    @Override
    public void addChannel(Channel channel) {
        channels.add(channel);
//        return channel;
    }

    // 채널 수정
    @Override
    public void updateChannel(UUID channelId, String channelName) {
        Channel ch= channels.stream().filter(ch1->ch1.getId().equals(channelId)).findFirst().get();

        if (ch != null) {
            System.out.println(ch.getChannelName() + " -> " + channelName);
            ch.update(channelName);
        } else if (channelName != null && !channelName.isEmpty()) {
            System.out.println("방이 없거나 입력이 잘못 되었습니다.");
        }
    }

    // 채널 삭제
    @Override
    public void deleteChannel(UUID channelId) {
        Channel ch= channels.stream()
                .filter(u -> u.getId().equals(channelId))
                .findFirst().get();

        if (ch != null) {
            System.out.println(ch.getChannelName() + "채널를 삭제했습니다.");
            channels.remove(ch);
        } else {
            System.out.println("없거나 삭제된 채널입니다.");
        }
    }

    // 채널 찾아서 객체 넘기기
    @Override
    public List<Channel> search(String name) {
        List<Channel> ch= channels.stream()
                .filter(u -> u.getChannelName().equals(name))
                .collect(Collectors.toList());

        if (ch.isEmpty()) {return null;}
        else {return ch;}
    }

    // 채널 찾기
    @Override
    public void searchChannel(String chName) {
        List<Channel> ch = search(chName);

        if (ch != null) {
            ch.stream().forEach(u -> System.out.println(u.getChannelName()+"방이 존재합니다."));
        } else {
            System.out.println("없거나 삭제 된 방입니다.");
        }
    }

    // 채널'들' 찾기
    @Override
    public void searchChannelS(List<String> names ) {
        names.forEach(name -> searchChannel(name));
    }

    // 업데이트가 된적 있는 채널 탐색
    @Override
    public void searchUpdateChannel() {
        AtomicBoolean notNull= new AtomicBoolean(false);

        channels.forEach(u -> {
            if (u.getCreated() != u.getUpdated()) {
                System.out.println("업데이트된 방: " + u.getChannelName());
                notNull.set(true);
            }
        });

        if(notNull.get() ==false){
            System.out.println("업데이트된 방이 없습니다.");
        }
    }

    @Override
    public List<Channel> getChannelList() {
        return this.channels;
    }
}