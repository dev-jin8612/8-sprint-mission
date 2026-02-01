package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController implements MessageApi {

  private final MessageService messageService;
  private final BinaryContentMapper binaryContentMapper;
  private final MessageMapper messageMapper;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDTO> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(name = "attachments", required = false) List<MultipartFile> imgList
  ) throws IOException {

    List<BinaryContentCreateRequest> binaryRequests = new ArrayList<>();

    if (imgList != null) {
      imgList.forEach(file ->
          binaryRequests.add(binaryContentMapper.toCreateRequest(file))
      );
    }

    Message message = messageService.create(messageCreateRequest, binaryRequests);
    log.info("메세지 보내기 성공");
    return ResponseEntity.ok(messageMapper.toDto(message));
  }

  @Override
  @PutMapping("/{messageId}")
  public ResponseEntity<MessageDTO> updateMessage(
      @PathVariable UUID messageId,
      @RequestParam MessageUpdateRequest newContent
  ) {
    Message message = messageService.update(messageId, newContent);
    log.info("{} 로 메세지 수정 성공", message.getContent());
    return ResponseEntity.ok(messageMapper.toDto(message));
  }

  @Override
  @GetMapping
  public ResponseEntity<List<MessageDTO>> searchMessage(
      @RequestParam UUID channelId
  ) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messageMapper.toDtoList(messages));
  }

  @Override
  @DeleteMapping("/{messageId}")
  public void deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    log.info("메세지 삭제 성공");
  }
}
