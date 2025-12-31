package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@ResponseBody
@RequestMapping("/userStatus")
@RequiredArgsConstructor
public class UserStatusController {

  private final UserStatusService userStatusService;

  // 유저 상태 수정
  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  public ResponseEntity<UserStatus> userStatusUpdate(@PathVariable UUID userId) {

    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());
    UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

    System.out.println(userStatus.getUpdatedAt() + " 상태 수정까지는 성공");
    return ResponseEntity.ok(userStatus);
  }
}
