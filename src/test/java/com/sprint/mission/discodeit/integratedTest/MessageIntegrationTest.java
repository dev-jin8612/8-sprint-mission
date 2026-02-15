package com.sprint.mission.discodeit.integratedTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MessageIntegrationTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  UserRepository userRepository;
  @Autowired
  UserStatusRepository userStatusRepository;
  @Autowired
  ChannelRepository channelRepository;
  @Autowired
  MessageRepository messageRepository;

  private User saveUserWithStatus() {
    User user = userRepository.save(new User("u1", "u1@test.com", "pw", null));
    userStatusRepository.save(new UserStatus(user, Instant.now()));
    return user;
  }

  private Channel savePublicChannel() {
    return channelRepository.save(new Channel(ChannelType.PUBLIC, "ch1", "desc1"));
  }

  private UUID createMessageApi(UUID channelId, UUID authorId, String content) throws Exception {
    MessageCreateRequest req = new MessageCreateRequest(content, channelId, authorId);

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "messageCreateRequest",
        "application/json",
        objectMapper.writeValueAsBytes(req)
    );

    String body = mockMvc.perform(multipart("/api/messages")
            .file(requestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andReturn().getResponse().getContentAsString();

    return UUID.fromString(objectMapper.readTree(body).get("id").asText());
  }

  @Test
  @Transactional
  void create_success_returns201_andJson() throws Exception {
    // given
    User author = saveUserWithStatus();
    Channel channel = savePublicChannel();

    MessageCreateRequest req = new MessageCreateRequest("hello", channel.getId(), author.getId());

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "messageCreateRequest",
        "application/json",
        objectMapper.writeValueAsBytes(req)
    );

    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(requestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.content").value("hello"))
        .andExpect(jsonPath("$.channelId").value(channel.getId().toString()));
  }

  @Test
  @Transactional
  void create_failure_returns404_whenChannelNotFound() throws Exception {
    // given
    User author = saveUserWithStatus();
    UUID notExistsChannelId = UUID.randomUUID();

    MessageCreateRequest req = new MessageCreateRequest("x", notExistsChannelId, author.getId());

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "messageCreateRequest",
        "application/json",
        objectMapper.writeValueAsBytes(req)
    );

    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(requestPart)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").exists())
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  @Transactional
  void update_success_returns200_andUpdatedContent() throws Exception {
    // given
    User author = saveUserWithStatus();
    Channel channel = savePublicChannel();
    UUID messageId = createMessageApi(channel.getId(), author.getId(), "old");

    MessageUpdateRequest updateReq = new MessageUpdateRequest("new");

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateReq)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("new"));
  }

  @Test
  @Transactional
  void update_failure_returns404_whenMessageNotFound() throws Exception {
    // given
    MessageUpdateRequest updateReq = new MessageUpdateRequest("new");

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateReq)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").exists())
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  @Transactional
  void delete_success_returns204_andEntityDeleted() throws Exception {
    // given
    User author = saveUserWithStatus();
    Channel channel = savePublicChannel();

    Message saved = messageRepository.save(new Message("to-delete", channel, author, List.of()));
    UUID messageId = saved.getId();

    // when
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());

    // then
    org.assertj.core.api.Assertions.assertThat(messageRepository.existsById(messageId)).isFalse();
  }

  @Test
  @Transactional
  void delete_failure_returns404_whenMessageNotFound() throws Exception {
    mockMvc.perform(delete("/api/messages/{messageId}", UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").exists())
        .andExpect(jsonPath("$.message").exists());
  }
}