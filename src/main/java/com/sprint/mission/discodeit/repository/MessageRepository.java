package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    // 메세지 등록
    void addMessage(Message message);

    // 메세지 정보 수정
    void updateMessage(UUID mesUid, String contents);

    // 메세지 삭제
    void deleteMessage(UUID mesUid);

    // 메세지 찾기
    List<Message> search(String content);
//    List<Message> searchL(String contents);

    void searchMessage(String content);

    // 메세지 여러개 찾기
    void searchMessageS(List<String> contents);

    // 수정된 메세지 찾기
    void searchUpdateMessage();
}
