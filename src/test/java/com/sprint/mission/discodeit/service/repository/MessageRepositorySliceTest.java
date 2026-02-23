package com.sprint.mission.discodeit.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditTestConfig.class)
class MessageRepositorySliceTest {

  @Autowired MessageRepository messageRepository;
  @Autowired UserRepository userRepository;
  @Autowired UserStatusRepository userStatusRepository;
  @Autowired ChannelRepository channelRepository;

  @Test
  void findAllByChannelIdWithAuthor_success_returnsSlice() {
    // given
    User author = userRepository.save(new User("testName","test11","pw",null));

    // author.status가 반드시 존재해야 join user_statuses를 통과함
    userStatusRepository.save(new UserStatus(author, Instant.now())); // 생성자/필드 맞게 수정

    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "testChannel", "test22"));

    messageRepository.save(new Message("m1", channel, author, List.of()));
    messageRepository.save(new Message("m2", channel, author, List.of()));
    messageRepository.save(new Message("m3", channel, author, List.of()));

    Pageable pageable = Pageable.ofSize(2);
    Instant cursor = Instant.now().plusSeconds(60);

    // when
    Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(), cursor, pageable
    );

    // then
    assertThat(slice.getContent()).hasSize(2);
    assertThat(slice.hasNext()).isTrue();
    assertThat(slice.getContent().get(0).getAuthor()).isNotNull();
  }

  @Test
  void findAllByChannelIdWithAuthor_failure_emptySlice_whenChannelIdNotExists() {
    // given
    Pageable pageable = Pageable.ofSize(10);

    // when
    Slice<Message> slice =
        messageRepository.findAllByChannelIdWithAuthor(
            UUID.randomUUID(),
            Instant.now(),
            pageable
        );

    // then
    assertThat(slice.getContent()).isEmpty();
    assertThat(slice.hasNext()).isFalse();
  }
}
