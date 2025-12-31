package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping("/create")
  public ResponseEntity<ReadStatus> createReadStatus(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  ) {
    // 생각해보니 방에 있는 유저인지 확인이랑 중복 생성 안되게 막아야 할거 같은데
    ReadStatus readStatus = readStatusService.create(
        new ReadStatusCreateRequest(
            readStatusCreateRequest.userId(),
            readStatusCreateRequest.channelId(),
            Instant.now()));

    log.info("유저의 수신상태 생성");
    return ResponseEntity.ok(readStatus);
  }

  @PutMapping("/update")
  public ResponseEntity<ReadStatus> updateReadStatus(@RequestParam UUID readStatusId) {
    ReadStatus readStatus = readStatusService.find(readStatusId);
    ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(Instant.now());
    readStatusService.update(readStatusId, request);

    log.info("유저의 수신상태 업데이트");
    return ResponseEntity.ok(readStatus);
  }

  @GetMapping("/search")
  public ResponseEntity<ReadStatus> searchReadStatus(
      @RequestParam UUID userId,
      @RequestParam UUID channelId
  ) {
    Optional<ReadStatus> readStatus = readStatusService.findAllByUserId(userId).stream()
        .filter(readStatus1 -> readStatus1.getChannelId().equals(channelId)).findFirst();

    if (readStatus.get().getLastReadAt().isAfter(readStatus.get().getUpdatedAt())) {
      log.info(readStatus.get().getId() + "님은 메세지를 봤습니다.");
    } else {
      log.info(readStatus.get().getId() + "님은 메세지를 안봤습니다.");
    }

    return ResponseEntity.ok(readStatus.get());
  }
}
