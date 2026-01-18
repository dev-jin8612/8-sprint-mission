package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/readStatuses")
@Tag(name = "ReadStatus API", description = "ReadStatus 관련 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "방문기록 생성", description = "유저의 채널 접속정보를 기록합니다.")
  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(
      @Parameter(description = "조회할 유저ID를 입력합니다.")
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  ) {
    // 생각해보니 방에 있는 유저인지 확인이랑 중복 생성 안되게 막아야 할거 같은데
    ReadStatus readStatus = readStatusService.create(
        new ReadStatusCreateRequest(
            readStatusCreateRequest.userId(),
            readStatusCreateRequest.channelId(),
            LocalDateTime.now()));

    log.info("유저의 수신상태 생성");
    return new ResponseEntity<>(readStatus, HttpStatus.CREATED);
  }

  @PatchMapping("/{readStatusId}")
  @Operation(summary = "방문기록 수정", description = "유저의 채널 접속정보를 수정합니다.")
  public ResponseEntity<ReadStatus> updateReadStatus(
      @Parameter(description = "조회할 방문기록ID를 입력합니다.")
      @PathVariable UUID readStatusId
  ) {
    ReadStatus readStatus = readStatusService.find(readStatusId);
    ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(LocalDateTime.now());
    readStatusService.update(readStatusId, request);

    log.info("유저의 수신상태 업데이트");
    return ResponseEntity.ok(readStatus);
  }

  @GetMapping(value = "/user")
  public ResponseEntity<List<ReadStatus>> findAllByUserId(
      @Parameter(description = "읽음 상태를 조회할 유저ID를 입력합니다.")
      @RequestParam UUID userId
  ) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }

  @GetMapping
  @Operation(summary = "방문기록 조회", description = "유저의 채널 접속정보를 조회합니다.")
  public ResponseEntity<ReadStatus> searchReadStatus(
      @Parameter(description = "방문 기록을 조회할 유저ID를 입력합니다.")
      @RequestParam UUID userId,
      @Parameter(description = "방문을 확인할 채널ID를 입력합니다.")
      @RequestParam UUID channelId
  ) {
    Optional<ReadStatus> readStatus = readStatusService.findAllByUserId(userId).stream()
        .filter(readStatus1 -> readStatus1.getChannel().getId().equals(channelId)).findFirst();

    if (readStatus.get().getLastReadAt().isAfter(readStatus.get().getUpdatedAt())) {
      log.info(readStatus.get().getId() + "님은 메세지를 봤습니다.");
    } else {
      log.info(readStatus.get().getId() + "님은 메세지를 안봤습니다.");
    }

    return ResponseEntity.ok(readStatus.get());
  }
}
