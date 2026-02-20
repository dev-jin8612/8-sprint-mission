package com.sprint.mission.discodeit.unitTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.Message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
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
public class MessageServiceTest {

  // 테스트에 필요한 객체 준비
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private PageResponseMapper pageResponseMapper;

  // 테스트 대상 객체
  @InjectMocks
  private BasicMessageService messageService;

  // 테스트 전 확인작업
  @BeforeEach
  @DisplayName("테스트 환경 설정 확인")
  void setUp() {
    // Mock 객체들이 정상적으로 주입되었는지 확인
    assertNotNull(messageRepository);
    assertNotNull(channelRepository);
    assertNotNull(userRepository);
    assertNotNull(messageMapper);
    assertNotNull(binaryContentStorage);
    assertNotNull(binaryContentRepository);
    assertNotNull(pageResponseMapper);
    assertNotNull(messageService);
  }

  // Given - 공통으로 사용
  String username = "username1";
  String password = "password2";
  String email = "email3";

  Channel publicChannel = spy(new Channel(ChannelType.PUBLIC, "test", "test"));
  User user = spy(new User(username, email, password, null));
  Message message = spy(new Message("test", publicChannel, user, null));

  UUID integatedId = UUID.randomUUID();

  @Nested
  @DisplayName("메세지 생성")
  class messageCreateTest {

    MessageCreateRequest messageCreateRequest =
        spy(new MessageCreateRequest("test", integatedId, integatedId));

    @Test
    @DisplayName("성공")
    void messageCreate_whenCreateSuccess_shouldReturnSuccess() {
      // given
      given(channelRepository.findById(any())).willReturn(Optional.ofNullable(publicChannel));
      given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));

      // when
      messageService.create(messageCreateRequest, List.of());

      // then
      then(messageRepository).should().save(any());
    }

    @Test
    @DisplayName("실패 - 없는 채널")
    void messageCreate_whenCreateFailToChannel_shouldReturnFailure() {
      // given
      given(channelRepository.findById(any())).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() ->
          messageService.create(messageCreateRequest, List.of())
      ).isInstanceOf(ChannelNotFoundException.class);

      // then
      then(messageRepository).should(never()).save(any());
      then(binaryContentRepository).should(never()).save(any());
      then(binaryContentStorage).should(never()).put(any(), any());
    }

    @Test
    @DisplayName("실패 - 없는 유저")
    void messageCreate_whenCreateFailToUser_shouldReturnFailure() {
      // given
      given(channelRepository.findById(any())).willReturn(Optional.of(publicChannel));
      given(userRepository.findById(messageCreateRequest.authorId()))
          .willReturn(Optional.empty());

      // when & then
      assertThrows(
          DiscodeitException.class,
          () -> messageService.create(messageCreateRequest, List.of())
      );

      // then
      then(messageRepository).should(never()).save(any());
      then(binaryContentRepository).should(never()).save(any());
      then(binaryContentStorage).should(never()).put(any(), any());
    }
  }

  @Nested
  @DisplayName("메세지 수정")
  class messageUpdateTest {

    MessageUpdateRequest messageUpdateRequest =
        spy(new MessageUpdateRequest("spy(new test 22"));

    @Test
    @DisplayName("성공")
    void messageUpdate_whenUpdateSuccess_shouldReturnSuccess() {
      // given
      given(messageRepository.findById(any())).willReturn(Optional.of(message));

      // when & then
      messageService.update(integatedId, messageUpdateRequest);

      // then
      then(message).should().update(any());
    }

    @Test
    @DisplayName("실패 - 없는 메세지")
    void messageUpdate_whenUpdateFailToMessage_shouldReturnFailure() {
      // given
      given(messageRepository.findById(any())).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() ->
          messageService.update(integatedId, messageUpdateRequest)
      ).isInstanceOf(MessageNotFoundException.class);

      // then
      then(message).should(never()).update(any());
    }
  }

  @Nested
  @DisplayName("메세지 삭제")
  class userdeleteTest {

    @Test
    @DisplayName("성공")
    void messageDelete_whenDeleteSuccess_shouldReturnSuccess() {
      // given
      given(messageRepository.existsById(any())).willReturn(true);

      // when
      messageService.delete(message.getId());

      // then
      then(messageRepository).should().deleteById(message.getId());
    }

    @Test
    @DisplayName("실패 - 없는 ID")
    void messageDelete_whenDeleteFail_shouldReturnFailure() {
      // given
      given(messageRepository.existsById(any())).willReturn(false);

      // when & then
      assertThatThrownBy(() ->
          messageService.delete(integatedId)
      ).isInstanceOf(MessageNotFoundException.class);

      // then
      then(messageRepository).should(never()).deleteById(integatedId);
    }
  }

}
