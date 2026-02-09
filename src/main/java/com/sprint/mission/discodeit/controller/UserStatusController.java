package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
  public ResponseEntity<UserStatusDto> userStatusUpdate(
      @Parameter(description = "접속 상태를 변경할 유저 ID입니다.")
      @PathVariable UUID userId,
      @Parameter(description = "접속 시간을 나타냅니다.")
      @Valid @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
  ) {
    UserStatusDto userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

    log.info(userStatus.id() + " 상태 수정까지는 성공");
    return ResponseEntity.ok(userStatus);
  }
}