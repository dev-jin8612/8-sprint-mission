package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.message.MessageUpdateReqeust;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    // 등록
    Message create(Message message);

    // 정보 수정
    Message update(MessageUpdateReqeust dto);

    // 삭제
    void delete(UUID id);

    // 내용으로 찾기
    List<Message> searchByContent(List<String> contents);

    // id로 찾기
    Message findById(UUID id);

    List<Message> getMessages();
}
