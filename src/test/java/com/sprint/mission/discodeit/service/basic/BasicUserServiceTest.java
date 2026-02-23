package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 단위 테스트")
public class BasicUserServiceTest {

  // 테스트에 필요한 객체 준비
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private UserMapper userMapper;

  // 테스트 대상 객체
  @InjectMocks
  private BasicUserService userService;

  // 테스트 전 확인작업
  @BeforeEach
  @DisplayName("테스트 환경 설정 확인")
  void setUp() {
    // Mock 객체들이 정상적으로 주입되었는지 확인
    assertNotNull(userRepository);
    assertNotNull(userStatusRepository);
    assertNotNull(binaryContentRepository);
    assertNotNull(binaryContentStorage);
    assertNotNull(userMapper);
    assertNotNull(userService);
  }

  // Given - 공통으로 사용
  String username = "username1";
  String password = "password2";
  String email = "email3";

  UUID id = UUID.randomUUID();

  User user = spy(new User(username, email, password, null));

  @Nested
  @DisplayName("유저 생성")
  class userCreateTest {

    UserCreateRequest userCreateRequest = spy(new UserCreateRequest(username, email, password));

    @Test
    @DisplayName("성공")
    void userCreate_whenCreateSuccess_shouldReturnSuccess() {
      // given
      given(userRepository.existsByEmail(email)).willReturn(false);
      given(userRepository.existsByUsername(username)).willReturn(false);

      // when
      userService.create(userCreateRequest, null);

      // then
      assertNotNull(userRepository.findByUsername(username));
    }

    @Test
    @DisplayName("실패 - 이메일")
    void userCreate_whenCreateFail_shouldReturnFailure() {
      // given
      given(userRepository.existsByUsername(username)).willReturn(false);

      // when & then
      assertThrows(
          DiscodeitException.class,
          () -> userService.create(userCreateRequest, null)
      );

      // then
      then(userRepository).should(never()).save(any());
      then(userStatusRepository).should(never()).save(any());
      then(binaryContentRepository).should(never()).save(any());
      then(binaryContentStorage).should(never()).put(any(), any());
      assertEquals(false, userRepository.existsByUsername(username));
    }

    @Test
    @DisplayName("실패 - 이름")
    void userCreate_whenCreateFail_returnLog() {
      // given
      given(userRepository.existsByEmail(username)).willReturn(true);

      // when & then
      assertThrows(
          DiscodeitException.class,
          () -> userService.create(userCreateRequest, null)
      );

      // then
      then(userRepository).should(never()).save(any());
      then(userStatusRepository).should(never()).save(any());
      then(binaryContentRepository).should(never()).save(any());
      then(binaryContentStorage).should(never()).put(any(), any());
      assertEquals(false, userRepository.existsByUsername(username));
    }
  }

  @Nested
  @DisplayName("유저 수정")
  class userUpdateTest {

    String newUsername = "name1";
    String newPassword = "pw";
    String newEmail = "33";

    UserUpdateRequest userUpdateRequest =
        spy(new UserUpdateRequest(newUsername, newEmail, newPassword));

    @Test
    @DisplayName("성공")
    void userCreate_whenCreateSuccess_shouldReturnSuccess() {
      // given
      given(userRepository.existsByEmail(any())).willReturn(false);
      given(userRepository.existsByUsername(any())).willReturn(false);
      given(userRepository.findById(any())).willReturn(Optional.of(user));

      // when
      userService.update(UUID.randomUUID(), userUpdateRequest, null);

      // then
      assertNotNull(userRepository.findByUsername(username));
    }

    @Test
    @DisplayName("실패 - 이메일")
    void userCreate_whenCreateFailToEmail_shouldReturnFailure() {
      // given
      given(userRepository.findById(any())).willReturn(Optional.of(user));
      given(userRepository.existsByEmail(any())).willReturn(true);

      // when & then
      assertThrows(
          DiscodeitException.class,
          () -> userService.update(UUID.randomUUID(), userUpdateRequest, null)
      );

      // then
      then(user).should(never()).update(any(), any(), any(), any());
      then(binaryContentRepository).should(never()).save(any());
      then(binaryContentStorage).should(never()).put(any(), any());
    }

    @Test
    @DisplayName("실패 - 이름")
    void userCreate_whenCreateFailToName_shouldReturnFailure() {
      // given
      given(userRepository.findById(any())).willReturn(Optional.of(user));
      given(userRepository.existsByEmail(any())).willReturn(false);
      given(userRepository.existsByUsername(any())).willReturn(true);

      // when & then
      assertThrows(
          DiscodeitException.class,
          () -> userService.update(UUID.randomUUID(), userUpdateRequest, null)
      );

      // then
      then(user).should(never()).update(any(), any(), any(), any());
      then(binaryContentRepository).should(never()).save(any());
      then(binaryContentStorage).should(never()).put(any(), any());
    }
  }

  @Nested
  @DisplayName("유저 삭제")
  class userdeleteTest {

    @Test
    @DisplayName("성공")
    void userCreate_whenCreateSuccess_shouldReturnSuccess() {
      // given
      given(userRepository.existsById(any())).willReturn(true);

      // when
      userService.delete(id);

      // then
      then(userRepository).should().deleteById(id);
    }

    @Test
    @DisplayName("실패 - 없는 ID")
    void userCreate_whenCreateFail_shouldReturnFailure() {
      // given
      given(userRepository.existsById(any())).willReturn(false);

      // when & then
      assertThrows(DiscodeitException.class, () -> userService.delete(id));

      // then
      then(userRepository).should(never()).deleteById(id);
    }
  }

}
