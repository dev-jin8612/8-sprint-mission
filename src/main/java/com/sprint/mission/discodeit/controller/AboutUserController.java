package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class AboutUserController {
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final AuthService authService;

    // 화면에 돌려주는거 없으니
    // ResponseBody 하고 void로 200만 나오게
    // 유저 생성
    @ResponseBody
    @RequestMapping(
            value = "/create",
            method = RequestMethod.GET,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void createUser(
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
    }

    // 유저 수정
    @ResponseBody
    @RequestMapping(
            value = "/update",
            method = RequestMethod.GET,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void updateUser(
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

        UserDto userDto = userService.findByUsername(userName);

        User test = userService.update(userDto.id(), userRequest, binaryContentCreateRequest);
        System.out.println(userDto.username() + "에서 " + test.getUsername() + " 수정까지는 성공");
    }


    // 전체 조회
    @ResponseBody
    @RequestMapping(
            value = "/allUser",
            method = RequestMethod.GET
    )
    public void allUser() {

        List<UserDto> userDto = userService.findAll();

        System.out.println("전체 유저 불러오기");
        userDto.forEach(System.out::println);
    }

    // 유저 상태 수정
    @ResponseBody
    @RequestMapping(value = "/userStatus/{userName}", method = RequestMethod.GET)
    // 이번엔 다른 어노테이션도 써보기 위해서 pathvariable사용
    public void userStatusUpdate(@PathVariable String userName) {
        UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(Instant.now());

        UserDto userdto = userService.findAll().stream()
                .filter(user -> user.username().equals(userName))
                .findFirst().get();

        UserStatus userStatus = userStatusService.updateByUserId(userdto.id(), userStatusUpdateRequest);
        System.out.println(userStatus.getUpdatedAt()+" 상태 수정까지는 성공");
    }

    // 유저 삭제
    @ResponseBody
    @RequestMapping(value = "/delete/{userName}", method = RequestMethod.GET)
    public void deleteUser(@PathVariable String userName) {

        UserDto userDto = userService.findAll().stream()
                .filter(user -> user.username().equals(userName))
                .findFirst().get();

        userService.delete(userDto.id());
        System.out.println("삭제까지는 성공");
    }

    // 로그인
    @ResponseBody
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    // 이번엔 다른 어노테이션도 써보기 위해서 RequestParam 사용
    public void auth(LoginRequest loginRequest) {
        User user = authService.login(loginRequest);
        System.out.println(user.getUsername()+" 로그인까지는 성공");
    }
}
