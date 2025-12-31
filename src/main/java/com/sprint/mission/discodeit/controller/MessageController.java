package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelReqeust;
import com.sprint.mission.discodeit.dto.data.UserReqeust;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @RequestMapping(
      value = "/send",
      method = RequestMethod.GET,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE

  )
  public ResponseEntity<Message> createMessage(
      @RequestParam String content,
      @RequestParam UUID channelId,
      @RequestParam UUID senderId,
      @RequestParam(required = false) List<MultipartFile> img
  ) throws IOException {

    List<BinaryContentCreateRequest> binaryContentCreateRequest = new ArrayList<>();
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(content, channelId,
        senderId);

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
    System.out.println("메세지 보내기 성공");

    return ResponseEntity.ok(message);
  }

  @RequestMapping(
      value = "/update",
      method = RequestMethod.GET

  )
  public ResponseEntity<Message> updateMessage(
      @RequestParam UUID messageId,
      @RequestParam MessageUpdateRequest newContent
  ) {
    Message message = messageService.update(messageId, newContent);
    System.out.println(message.getContent() + "로 메세지 수정 성공");
    return ResponseEntity.ok(message);
  }

  @RequestMapping(value = "/search/{channelId}")
  public ResponseEntity<List<Message>> searchMessage(@PathVariable UUID channelId) {
    List<Message> message = messageService.findAllByChannelId(channelId);
    message.stream().forEach(message1 -> System.out.println(message1.getContent()));
    return ResponseEntity.ok(message);
  }

  @RequestMapping(value = "/delete/{messageId}")
  public void deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    System.out.println("메세지 삭제 성공");
  }
}
