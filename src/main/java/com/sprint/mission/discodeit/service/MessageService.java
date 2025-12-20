package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.meg.MegBCDTO;
import com.sprint.mission.discodeit.dto.meg.MegUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 등록
    Message create(MegBCDTO dto);

    // 정보 수정
    Message update(MegUpdateDTO dto);

    // 삭제
    void delete(UUID id);

    // 내용으로 찾기
    List<Message> searchByContent(List<String> contents);

    // id로 찾기
    Message findById(UUID id);

    List<Message> getMessages();
}
