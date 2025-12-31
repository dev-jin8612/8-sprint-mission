package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserResponse;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  // 유저 생성
  @PostMapping(
      value = "/create",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<User> createUser(
      @RequestBody UserCreateRequest userRequest,
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
    log.info(user.getUsername() + " 생성까지는 성공");

    return ResponseEntity.ok(user);
  }

  // 유저 수정
  @PutMapping(
      value = "/update",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<User> updateUser(
      @RequestParam String userName,
      @RequestPart UserUpdateRequest userRequest,
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

    UserResponse userResponse = userService.findByUsername(userName);
    User test = userService.update(userResponse.id(), userRequest, binaryContentCreateRequest);

    log.info(userResponse.username() + "에서 " + test.getUsername() + " 수정까지는 성공");
    return ResponseEntity.ok(test);
  }

  // 전체 조회
  @GetMapping("/findAll")
  public ResponseEntity<List<UserResponse>> finaAll() {
    List<UserResponse> userResponse = userService.findAll();

    System.out.println("전체 유저 불러오기");
    userResponse.forEach(System.out::println);

    return ResponseEntity.ok(userResponse);
  }

  // 유저 삭제
  @DeleteMapping("/delete/{userName}")
  public void deleteUser(@PathVariable String userName) {
    UserResponse userResponse = userService.findByUsername(userName);
    userService.delete(userResponse.id());
    log.info("삭제까지 성공");
  }
}