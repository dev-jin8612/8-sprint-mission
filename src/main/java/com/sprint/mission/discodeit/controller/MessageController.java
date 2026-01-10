package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Message API", description = "Message 관련 API")
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "Message 생성", description = "메세지를 생성합니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Message> createMessage(
      @Parameter(description = "메세지 내용와 보낸 채널, 보낸 사람의 정보입니다.")
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @Parameter(description = "메세지로 보낸 파일입니다.")
      @RequestPart(name = "attachments", required = false) List<MultipartFile> img

  ) throws IOException {
    List<BinaryContentCreateRequest> binaryContentCreateRequest = new ArrayList<>();

    if (img != null) {
      img.forEach(upload -> {
        try {
          binaryContentCreateRequest.add(
              new BinaryContentCreateRequest(
                  upload.getOriginalFilename(),
                  upload.getContentType(),
                  upload.getBytes()
              ));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    }
    Message message = messageService.create(messageCreateRequest, binaryContentCreateRequest);

    log.info("메세지 보내기 성공");
    return ResponseEntity.ok(message);
  }

  @PutMapping("/{messageId}")
  @Operation(summary = "Message 수정", description = "메세지를 수정합니다.")
  public ResponseEntity<Message> updateMessage(
      @Parameter(description = "수정할 메세지 id입니다.")
      @PathVariable UUID messageId,
      @Parameter(description = "수정할 메세지 내용 입니다.")
      @RequestParam MessageUpdateRequest newContent
  ) {
    Message message = messageService.update(messageId, newContent);
    log.info(message.getContent() + "로 메세지 수정 성공");
    return ResponseEntity.ok(message);
  }

  @Operation(summary = "Message 탐색", description = "채널 안의 메세지를 전부 찾습니다.")
  @GetMapping
  public ResponseEntity<List<Message>> searchMessage(
      @Parameter(description = "찾을 채널의 ID입니다.")
      @RequestParam UUID channelId
  ) {
    List<Message> message = messageService.findAllByChannelId(channelId);
    message.stream().forEach(message1 -> System.out.println(message1.getContent()));
    return ResponseEntity.ok(message);
  }

  @DeleteMapping("/{messageId}")
  @Operation(summary = "Message 삭제", description = "메세지를 삭제합니다.")
  public void deleteMessage(
      @Parameter(description = "삭제할 메세지의 ID입니다.")
      @PathVariable UUID messageId
  ) {
    messageService.delete(messageId);
    log.info("메세지 식제 성공");
  }
}
