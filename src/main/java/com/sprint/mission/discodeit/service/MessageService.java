package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 등록
    Message create(String meg, Channel ch, UUID userId);

    // 정보 수정
    Message update(UUID mesUid, String contents);

    // 삭제
    void delete(UUID id);

    // 내용으로 찾기
    List<Message> searchByContent(List<String> contents);

    // id로 찾기
    Message findById(UUID id);

    List<Message> getMessages();
}
