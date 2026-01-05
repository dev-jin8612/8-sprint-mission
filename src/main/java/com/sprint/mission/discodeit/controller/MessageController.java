package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping
  public ResponseEntity<Message> createMessage(
      @RequestParam MessageCreateRequest request,
      @RequestPart(required = false) List<MultipartFile> img

  ) throws IOException {
    List<BinaryContentCreateRequest> binaryContentCreateRequest = new ArrayList<>();
    MessageCreateRequest messageCreateRequest = request;

    img.forEach(upload -> {
      try {
        binaryContentCreateRequest.add(new BinaryContentCreateRequest(
            upload.getOriginalFilename(),
            upload.getContentType(),
            upload.getBytes()
        ));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    Message message = messageService.create(messageCreateRequest, binaryContentCreateRequest);

    log.info("메세지 보내기 성공");
    return ResponseEntity.ok(message);
  }

  @PutMapping("/{messageId}")
  public ResponseEntity<Message> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest newContent
  ) {
    Message message = messageService.update(messageId, newContent);
    log.info(message.getContent() + "로 메세지 수정 성공");
    return ResponseEntity.ok(message);
  }

  @GetMapping("/{channelId}")
  public ResponseEntity<List<Message>> searchMessage(@PathVariable UUID channelId) {
    List<Message> message = messageService.findAllByChannelId(channelId);
    message.stream().forEach(message1 -> System.out.println(message1.getContent()));
    return ResponseEntity.ok(message);
  }

  @DeleteMapping("/{messageId}")
  public void deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    log.info("메세지 식제 성공");
  }
}
