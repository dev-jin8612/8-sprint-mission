package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserResponse;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "로그인 관련 API")
public class AuthController {

  private final AuthService authService;

  // 로그인
  @RequestMapping
  @Operation(summary = "login", description = "로그인을 요청합니다.")
  @Parameter(
      name = "name",
      description = "로그인할 계정의 이름입니다.",
      example = "/auth?username=황&password=1234",
      required = true
  )
  @Parameter(
      name = "description",
      description = "로그인할 계정의 비밀번호입니다.",
      example = "/auth?username=황&password=1234",
      required = true
  )
  public ResponseEntity<UserResponse> auth(
      @RequestBody LoginRequest loginRequest) {
    User user = authService.login(loginRequest);

    UserResponse userResponse = new UserResponse(
        user.getId(), user.getCreatedAt(),
        user.getUpdatedAt(), user.getUsername(),
        user.getEmail(), user.getProfileId(), true);

    log.info("로그인 성공");
    return ResponseEntity.ok(userResponse);
  }
}
