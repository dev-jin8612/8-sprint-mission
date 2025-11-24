package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface ChannelService {
    // 채널
    // 등록
    void addChannel(Channel channel);

    // 정보 수정
    void updateChannel(String channel, String channelname);

    // 삭제
    void deleteChannel(String channel);

    // 찾기
    Channel search(String name);

    // 방찾기
    void searchchannel(String name);

    // 여러 방 찾기
    void searchChannelS(List<String> names );

    // 수정된 방 찾기
    void searchUpdateChannel();
    
    // 방에 있는 유저
    // 추가
    void addUser(User user);
    void updateUser(User user); //흠 이건 필요한가?
    // 삭제
    void deleteUser(User user);

    // 메세지 내용
    // 추가
    void addMessage(Message message);
    void updateMessage(Message message);//필요한가
    // 삭제
    void deleteMessage(Message message);
}
