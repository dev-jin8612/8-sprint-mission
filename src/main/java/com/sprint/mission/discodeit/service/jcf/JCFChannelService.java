package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    final List<Channel> channels = new ArrayList<>();
    final List<User> uesrList = new ArrayList<>();
    final List<Message> megList = new ArrayList<>();

    // 채널 추가
    @Override
    public void addChannel(Channel channel) {
        channels.add(channel);
    }


    // 채널 수정
    @Override
    public void updateChannel(String channel, String channelName) {
        Channel found = search(channel);

        if (found != null) {
            System.out.println(found.getChannelName() + " -> " + channelName);
            found.update(channelName);
        } else if (channelName != null) {
            System.out.println("입력이 잘못 되었습니다.");
        }

    }

    // 채널 삭제
    @Override
    public void deleteChannel(String name) {
        Channel channel = search(name);

        if (channel != null) {
            System.out.println(channel.getChannelName() + "채널를 삭제했습니다.");
            channels.remove(channel);
        } else {
            System.out.println("없거나 삭제된 채널입니다.");
        }
    }

    // 채널 찾아서 객체 넘기기
    @Override
    public Channel search(String name) {
//        이거는 여러명 검색을 위한 수정도 필요할 듯
//        아무래도 단체용은 따로 하는게 좋을것 같다.
        Channel channel = channels.stream().filter(u -> {
            return u.getChannelName().equals(name);
        }).collect(Collectors.toList()).get(0);

//        System.out.println(channel);
        
        return channel;
    }

    // 채널 찾기
    @Override
    public void searchChannel(String name) {
        Channel channel = search(name);

        if (channel != null) {
            System.out.println(channel.getChannelName() + "방이 존재합니다.");
        } else {
            System.out.println("없거나 삭제 된 방입니다.");
        }
    }

    // 채널'들' 찾기
    @Override
    public void searchChannelS(List<String> names) {
        // 여기서 여러번 부르는 것보다는
        // serach에서 list로 보내주는게 좋을거 같기는 한데
        // 일단 이렇게만
        names.forEach(name -> {
            Channel channel = search(name);
            System.out.println(channel.getChannelName() + "방이 존재합니다.");
        });
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

    // 유저추가(방 입장/초대/생성 시)
    @Override
    public void addUser(User user) {

    }

    // 유저 삭제<강퇴,탈퇴>
    @Override
    public void deleteUser(User user) {

    }

    // 메세지 보내기
    @Override
    public void addMessage(Message message) {

    }

    //메세지 수정
    @Override
    public void updateMessage(Message message) {

    }

    // 메세지 삭제
    @Override
    public void deleteMessage(Message message) {

    }
}