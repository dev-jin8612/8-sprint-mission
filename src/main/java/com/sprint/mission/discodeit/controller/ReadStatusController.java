package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping
  @Operation(summary = "방문기록 생성", description = "유저의 채널 접속정보를 기록합니다.")
  @Parameter(name = "userId", description = "조회할 유저ID를 입력합니다.", required = true)
  @Parameter(name = "channelId",
      description = "조회할 채널ID를 입력합니다.",
      example = "/readStatus?userId=18ed1a91-982d-4f61-8440-0c7a508135e8&channelId=1b7812e2-7bc7-4ddc-a784-f5788f300aef",
      required = true
  )
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
    return new ResponseEntity<>(readStatus, HttpStatus.CREATED);
  }

  @PutMapping("/{readStatusId}")
  @Operation(summary = "방문기록 수정", description = "유저의 채널 접속정보를 수정합니다.")
  @Parameter(name = "readStatusId", description = "조회할 방문기록ID를 입력합니다.", required = true)
  public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID readStatusId) {
    ReadStatus readStatus = readStatusService.find(readStatusId);
    ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(Instant.now());
    readStatusService.update(readStatusId, request);

    log.info("유저의 수신상태 업데이트");
    return ResponseEntity.ok(readStatus);
  }

  @GetMapping(value = "/user")
  public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }

  @GetMapping
  @Operation(summary = "방문기록 조회", description = "유저의 채널 접속정보를 조회합니다.")
  @Parameter(name = "userId", description = "조회할 유저ID를 입력합니다.", required = true)
  @Parameter(name = "channelId",
      description = "조회할 채널ID를 입력합니다.",
      example = "/readStatus?userId=18ed1a91-982d-4f61-8440-0c7a508135e8&channelId=1b7812e2-7bc7-4ddc-a784-f5788f300aef",
      required = true
  )
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
