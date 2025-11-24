package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.ArrayList;
import java.util.UUID;

public interface MessageService {
    // 메세지 등록
    void addMessage(Message message);

    // 메세지 정보 수정
    void updateMessage(UUID mesUid, String contents);

    // 메세지 삭제
    void deleteMessage(UUID mesUid);

    // 메세지 찾기
    Message search(UUID mesUId);

    void searchMessage(UUID mesUId);

    // 메세지 여러개 찾기
    void searchMessageS(ArrayList<Message> megs );

    // 수정된 메세지 찾기
    void searchUpdateMessage();

    // 수정된 메세지들 찾기
//    void searchUpdateMessageS(List<String> names);
}
