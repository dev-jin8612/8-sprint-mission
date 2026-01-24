package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @PostMapping
  public ResponseEntity<ReadStatusDTO> createReadStatus(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  ) {
    ReadStatus readStatus = readStatusService.create(
        new ReadStatusCreateRequest(
            readStatusCreateRequest.userId(),
            readStatusCreateRequest.channelId(),
            Instant.now()
        )
    );

    log.info("유저의 수신상태 생성");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusMapper.toDto(readStatus));
  }

  @Override
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusDTO> updateReadStatus(
      @PathVariable UUID readStatusId
  ) {
    ReadStatusUpdateRequest request =
        new ReadStatusUpdateRequest(Instant.now());

    readStatusService.update(readStatusId, request);
    ReadStatus readStatus = readStatusService.find(readStatusId);

    log.info("유저의 수신상태 업데이트");
    return ResponseEntity.ok(readStatusMapper.toDto(readStatus));
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatusDTO>> findAllByUserId(
      @RequestParam UUID userId
  ) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);

    for (ReadStatus readStatus : readStatuses) {
    log.info(readStatus.toString());

    }
    return ResponseEntity.ok(readStatusMapper.toDtoList(readStatuses));
  }
}
