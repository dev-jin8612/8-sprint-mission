package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.spy;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
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
@DisplayName("ChannelService 단위 테스트")
public class BasicChannelServiceTest {

  // 테스트에 필요한 객체 준비
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelMapper channelMapper;

  // 테스트 대상 객체
  @InjectMocks
  private BasicChannelService channelService;

  // 테스트 전 확인작업
  @BeforeEach
  @DisplayName("테스트 환경 설정 확인")
  void setUp() {
    // Mock 객체들이 정상적으로 주입되었는지 확인
    assertNotNull(channelRepository);
    assertNotNull(readStatusRepository);
    assertNotNull(messageRepository);
    assertNotNull(userRepository);
    assertNotNull(channelMapper);
    assertNotNull(channelService);
  }

  // Given - 공통으로 사용
  String channelname = "channelname1";
  String description = "test";
  ChannelType privateType = ChannelType.PRIVATE;
  ChannelType publicType = ChannelType.PUBLIC;

  Channel privateChannel = spy(new Channel( privateType,channelname, description));
  Channel publicChannel = spy(new Channel( publicType,channelname, description));

  User user = spy(new User("testName","@@@","1234",null));
  UserStatus userStatus = spy(new UserStatus(user,Instant.now()));
  ReadStatus readStatus = spy(new ReadStatus(user,privateChannel, Instant.now()));

  UUID id = UUID.randomUUID();
  List<UUID> userList = List.of(id);

  @Nested
  @DisplayName("채널 생성")
  class channelCreateTest {

    @Test
    @DisplayName("성공 - 공개")
    void publicChannel_whenCreateSuccess_shouldReturnSuccess() {
      // given
      PublicChannelCreateRequest request =
          spy(spy(new PublicChannelCreateRequest(channelname, description)));

      // when
      channelService.create(request);

      // then
      then(channelRepository).should().save(any());
    }

    @Test
    @DisplayName("성공 - 비공개")
    void privateChannel_whenCreateSuccess_shouldReturnSuccess() {
      // given
      PrivateChannelCreateRequest request = spy(new PrivateChannelCreateRequest(userList));
      given(userRepository.findAllById(any())).willReturn(List.of(user));

      // when
      channelService.create(request);

      // then
      then(channelRepository).should().save(any());
      then(readStatusRepository).should().saveAll(any());
    }
  }

  @Nested
  @DisplayName("채널 수정")
  class channelUpdateTest {

    String newChannelname = "test22";
    String newDescription = "zzzz";

    PublicChannelUpdateRequest channelUpdateRequest =
        spy(new PublicChannelUpdateRequest(newChannelname, newDescription));


    Channel publicChannelUpdate = spy(new Channel( publicType,newChannelname, newDescription));

    @Test
    @DisplayName("성공")
    void channelUpdate_whenUpdateSuccess_shouldReturnSuccess() {
      // given
      given(channelRepository.findById(id)).willReturn(Optional.of(publicChannel));

      // when
      channelService.update(id, channelUpdateRequest);

      // then
      then(channelRepository).should().findById(id);
      then(channelMapper).should().toDTO(any());
    }

    @Test
    @DisplayName("실패 - 없는 채널")
    void channelUpdate_whenUpdateFailToChannel_shouldReturnFailure() {
      // given
      given(channelRepository.findById(id)).willReturn(Optional.empty());

      // when & then
      assertThrows(
          DiscodeitException.class,
          () -> channelService.update(id, channelUpdateRequest)
      );

      // then
      then(channelMapper).should(never()).toDTO(any());
    }

    @Test
    @DisplayName("실패 - 비공개 채널")
    void channelUpdate_whenUpdateFailToPrivate_shouldReturnFailure() {
      // given
      given(channelRepository.findById(any())).willReturn(Optional.ofNullable(privateChannel));

      // when & then
      assertThrows(
          DiscodeitException.class,
          () -> channelService.update(id, channelUpdateRequest)
      );

      // then
      then(privateChannel).should(never()).update(any(), any());
    }
  }

  @Nested
  @DisplayName("채널 삭제")
  class channeldeleteTest {

    @Test
    @DisplayName("성공")
    void channelDelete_whenDeleteSuccess_shouldReturnSuccess() {
      // given
      given(channelRepository.existsById(any())).willReturn(true);

      // when
      channelService.delete(id);

      then(channelRepository).should().deleteById(any());
      then(messageRepository).should().deleteAllByChannelId(any());
      then(readStatusRepository).should().deleteAllByChannelId(any());
    }

    @Test
    @DisplayName("실패 - 없는 ID")
    void channelDelete_whenDeleteFail_shouldReturnFailure() {
      // given
      given(channelRepository.existsById(any())).willReturn(false);

      // when & then
      assertThrows(DiscodeitException.class, () -> channelService.delete(id));

      then(channelRepository).should(never()).deleteById(any());
      then(messageRepository).should(never()).deleteAllByChannelId(any());
      then(readStatusRepository).should(never()).deleteAllByChannelId(any());
    }
  }

}
