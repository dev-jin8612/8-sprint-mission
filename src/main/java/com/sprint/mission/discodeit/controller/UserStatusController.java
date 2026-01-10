package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/userStatus")
@Tag(name = "UserStatus API", description = "UserStatus 관련 API")
public class UserStatusController {

  private final UserStatusService userStatusService;

  // 유저 상태 수정
  @PatchMapping("/{userId}")
  @Operation(summary = "유저 접속상태 변경", description = "유저의 접속정보를 변경합니다.")
  @Parameter(name = "userId",
      description = "변경할 유저ID를 입력합니다.",
      example = "/userStatus/18ed1a91-982d-4f61-8440-0c7a508135e8",
      required = true
  )
  public ResponseEntity<UserStatus> userStatusUpdate(@PathVariable UUID userId) {
    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());
    UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

    log.info(userStatus.getUpdatedAt() + " 상태 수정까지는 성공");
    return ResponseEntity.ok(userStatus);
  }
}