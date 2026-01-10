package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "로그인 관련 API")
public class AuthController {

  private final AuthService authService;

  // 로그인
  @PostMapping(value = "login")
  @Operation(summary = "login", description = "로그인을 요청합니다.")
  public ResponseEntity<User> auth(
      @Parameter(
          name = "password",
          description = "로그인할 계정의 비밀번호입니다.",
          example = "/auth?username=황&password=1234",
          required = true
      )
      @RequestBody LoginRequest loginRequest) {
    User user = authService.login(loginRequest);

    log.info("로그인 성공");
    return ResponseEntity.ok(user);
  }
}
