package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserReqeust;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  // 로그인
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<User> auth(LoginRequest loginRequest) {
    User user = authService.login(loginRequest);
    System.out.println(user.getUsername() + " 로그인까지는 성공");
    return ResponseEntity.ok(user);
  }
}
