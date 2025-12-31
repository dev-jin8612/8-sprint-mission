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

@RestController
@ResponseBody
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  // 유저 생성
  @RequestMapping(
      value = "/create",
      method = RequestMethod.GET,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE

  )
  public ResponseEntity<User> createUser(
      UserCreateRequest userRequest,
      /*@ReuestParam은 String으로 값을 받기 때문에
       * DTO 같은 객체를 읽을 때는 그냥 그대로 적어 넣기*/
      @RequestParam(required = false) MultipartFile img

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
    System.out.println(user.getUsername() + " 생성까지는 성공");

    return ResponseEntity.ok(user);
  }

  // 유저 수정
  @RequestMapping(
      value = "/update",
      method = RequestMethod.GET,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE

  )
  public ResponseEntity<User> updateUser(
      @RequestParam String userName,
      UserUpdateRequest userRequest,
      @RequestParam(required = false) MultipartFile img

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

    UserReqeust userReqeust = userService.findByUsername(userName);

    User test = userService.update(userReqeust.id(), userRequest, binaryContentCreateRequest);
    System.out.println(userReqeust.username() + "에서 " + test.getUsername() + " 수정까지는 성공");

    return ResponseEntity.ok(test);
  }

  // 전체 조회
  @RequestMapping(
      value = "/findAll",
      method = RequestMethod.GET

  )
  public ResponseEntity<List<UserReqeust>> finaAll() {
    List<UserReqeust> userReqeust = userService.findAll();

    System.out.println("전체 유저 불러오기");
    userReqeust.forEach(System.out::println);

    return ResponseEntity.ok(userReqeust);
  }


  // 유저 삭제
  @RequestMapping(value = "/delete/{userName}", method = RequestMethod.GET)
  public void deleteUser(@PathVariable String userName) {
    UserReqeust userReqeust = userService.findByUsername(userName);
    userService.delete(userReqeust.id());
    System.out.println("삭제까지는 성공");
  }
}
