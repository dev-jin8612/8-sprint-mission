package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserResponse;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  // 로그인
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<UserResponse> auth(@RequestBody LoginRequest loginRequest) {
    User user = authService.login(loginRequest);
    UserResponse userResponse = new UserResponse(
        user.getId(), user.getCreatedAt(),
        user.getUpdatedAt(), user.getUsername(),
        user.getEmail(), user.getProfileId(), true);

    log.info("로그인 성공");
    return ResponseEntity.ok(userResponse);
  }
}
