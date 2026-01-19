package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
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
import org.springframework.web.bind.annotation.RequestMapping;
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

  // 유저 생성
  @Operation(summary = "User 생성", description = "유저을 생성합니다.")
  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDTO> createUser(

      @Parameter(description = "생성할 유저의 정보입니다.")
      @RequestPart("userCreateRequest") UserCreateRequest userRequest,

      @Parameter(description = "생성할 유저의 프로필입니다.")
      @RequestPart(name = "profile") MultipartFile img

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

    UserDTO userDTO = new UserDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile().getId(),
        false);

    log.info(user.getUsername() + " 생성까지는 성공");
    return ResponseEntity.ok(userDTO);
  }

  // 유저 수정
  @Operation(summary = "User 수정", description = "유저을 수정합니다.")
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDTO> updateUser(
      @Parameter(description = "수정할 유저 UUID입니다..")
      @PathVariable UUID userId,

      @Parameter(description = "유저가 변경할 정보입니다.")
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,

      @Parameter(description = "변경할 프로필 이미지 입니다.")
      @RequestPart(name = "profile", required = false) MultipartFile img

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

    UserDTO userDTO = userService.find(userId);
    User test = userService.update(userDTO.id(), userUpdateRequest,
        binaryContentCreateRequest);

    log.info(userDTO.username() + "에서 " + test.getUsername() + " 수정까지는 성공");
    return ResponseEntity.ok(userDTO);
  }

  // 전체 조회
  @GetMapping(value = "/findAll")
  @Operation(summary = "유저 전체 조회", description = "모든 유저의 정보를 가져옵니다.")
  public ResponseEntity<List<UserDTO>> finaAll() {
    try {
      List<UserDTO> userDTO = userService.findAll();
      return ResponseEntity.ok(userDTO);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  // 유저 삭제
  @DeleteMapping("/{userName}")
  @Operation(summary = "User 삭제", description = "유저을 삭제합니다.")
  public void deleteUser(
      @Parameter(description = "삭제할 유저의 id를 입력합니다.")
      @PathVariable UUID userId
  ) {
    UserDTO userDTO = userService.find(userId);
    userService.delete(userDTO.id());
    log.info("삭제까지 성공");
  }
}