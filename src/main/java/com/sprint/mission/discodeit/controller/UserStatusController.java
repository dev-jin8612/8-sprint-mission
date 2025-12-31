package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/userStatus")
@RequiredArgsConstructor
public class UserStatusController {

  private final UserStatusService userStatusService;

  // 유저 상태 수정
  @PutMapping("/{userId}")
  public ResponseEntity<UserStatus> userStatusUpdate(@PathVariable UUID userId) {
    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());
    UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

    log.info(userStatus.getUpdatedAt() + " 상태 수정까지는 성공");
    return ResponseEntity.ok(userStatus);
  }
}