package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserResponse;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User API", description = "User 관련 API")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  // 유저 생성
  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "User 생성", description = "유저을 생성합니다.")
  @Parameter(name = "username", description = "생성할 유저 이름(닉네임)을 입력합니다.", required = true)
  @Parameter(name = "email", description = "생성할 유저의 이메일을 입력합니다.", required = true)
  @Parameter(name = "password",
      description = "생성할 유저의 비밀번호를 입력합니다.",
      example = "/user?username=황&email=test@gmail.com&password=1234",//body로 수정
      required = true
  )
  @Parameter(name = "img", description = "생성할 유저의 프로필을 입력합니다.", required = false)
  public ResponseEntity<UserResponse> createUser(
      UserCreateRequest userRequest,
      /*@ReuestParam은 String으로 값을 받기 때문에
       * DTO 같은 객체를 읽을 때는 그냥 그대로 적어 넣기*/
      @RequestPart(required = false) MultipartFile img

  ) throws IOException {
    Optional<BinaryContentCreateRequest> binaryContentCreateRequest =
        Optional.ofNullable(img)
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });

    User user = userService.create(userRequest, binaryContentCreateRequest);

    UserResponse userResponse = new UserResponse(
        user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
        user.getUsername(), user.getEmail(), user.getProfileId(),
        false);

    log.info(user.getUsername() + " 생성까지는 성공");
    return ResponseEntity.ok(userResponse);
  }

  // 유저 수정
  @PatchMapping(value = "/{userId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "User 수정", description = "유저을 수정합니다.")
  @Parameter(name = "username", description = "수정할 유저 이름(닉네임)을 입력합니다.", required = true)
  @Parameter(name = "newUsername", description = "유저의 새이름(새닉네임)을 입력합니다.", required = true)
  @Parameter(name = "newEmail", description = "유저의 새이메일을 입력합니다.", required = true)
  @Parameter(name = "newPassword",
      description = "유저의 새비밀번호를 입력합니다.",
      example = "/user?channelId=18ed1a91-982d-4f61-8440-0c7a508135e8",// 여기 body인데 어케 수정?
      required = true
  )
  @Parameter(name = "img", description = "유저의 새프로필을 입력합니다.", required = false)
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable UUID userId,
      UserUpdateRequest userRequest,
      @RequestPart(required = false) MultipartFile img

  ) throws IOException {
    Optional<BinaryContentCreateRequest> binaryContentCreateRequest =
        Optional.ofNullable(img)
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });

    UserResponse userResponse = userService.find(userId);
    User test = userService.update(userResponse.id(), userRequest, binaryContentCreateRequest);

    log.info(userResponse.username() + "에서 " + test.getUsername() + " 수정까지는 성공");
    return ResponseEntity.ok(userResponse);
  }

  // 전체 조회
  @GetMapping(value = "/findAll")
  @Operation(summary = "유저 전체 조회", description = "모든 유저의 정보를 가져옵니다.")
  public ResponseEntity<List<UserResponse>> finaAll() {
//    List<UserResponse> userResponse = userService.findAll();
//
//    System.out.println("전체 유저 불러오기");
//    userResponse.forEach(System.out::println);
//
//    return ResponseEntity.ok(userResponse);

    try {
      List<UserResponse> userResponse = userService.findAll();
      return ResponseEntity.ok(userResponse);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  // 유저 삭제
  @DeleteMapping("/{userName}")
  @Operation(summary = "User 삭제", description = "유저을 삭제합니다.")
  @Parameter(name = "userId",
      description = "삭제할 유저의 id를 입력합니다.",
      example = "/user/18ed1a91-982d-4f61-8440-0c7a508135e8",
      required = true
  )
  public void deleteUser(@PathVariable UUID userId) {
    UserResponse userResponse = userService.find(userId);
    userService.delete(userResponse.id());
    log.info("삭제까지 성공");
  }
}