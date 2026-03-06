package com.sprint.mission.discodeit.integratedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChannelIntegrationTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ChannelRepository channelRepository;
  @Autowired
  ReadStatusRepository readStatusRepository;
  @Autowired
  MessageRepository messageRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  UserService userService;
  @Autowired
  UserMapper userMapper;

  private User saveUser(String username, String email) {
    userService.create(new UserCreateRequest(username, email, "pw"), null);
    return userRepository.findByUsername(username).orElse(null);
  }

  @Test
  @Transactional
  void createPublic_thenUpdate_thenDelete_flow_success() throws Exception {
    // 1) 공개 채널 생성
    PublicChannelCreateRequest createReq = new PublicChannelCreateRequest("ch1", "desc1");

    String createdJson = mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createReq)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.name").value("ch1"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    UUID channelId = UUID.fromString(objectMapper.readTree(createdJson).get("id").asText());

    // DB에 실제로 저장됐는지 확인
    Channel saved = channelRepository.findById(channelId).orElseThrow();
    assertThat(saved.getType()).isEqualTo(ChannelType.PUBLIC);

    // 2) 공개 채널 수정
    PublicChannelUpdateRequest updateReq = new PublicChannelUpdateRequest("ch1-new", "desc1-new");

    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateReq)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("ch1-new"));

    // 3) 삭제 전: readStatus/message 삭제 로직이 돌도록 더미 데이터 넣기
    User u1 = saveUser("u1", "u1@test.com");
    readStatusRepository.save(new ReadStatus(u1, saved, Instant.now()));

    // 4) 채널 삭제
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    // 5) DB에서 삭제 확인
    assertThat(channelRepository.existsById(channelId)).isFalse();
  }

  @Test
  @Transactional
  void createPrivate_thenFindAll_includesPrivateForParticipant_andPublic() throws Exception {
    // given: 유저 2명 생성
    User u1 = saveUser("u1", "u1@test.com");
    User u2 = saveUser("u2", "u2@test.com");

    // public 채널 2개 미리 생성(DB 직접 저장)
    channelRepository.save(new Channel(ChannelType.PUBLIC, "pub1", "d1"));
    channelRepository.save(new Channel(ChannelType.PUBLIC, "pub2", "d2"));

    // when: private 채널 생성 API 호출 (participants: u1, u2)
    PrivateChannelCreateRequest privateReq =
        new PrivateChannelCreateRequest(List.of(u1.getId(), u2.getId()));

    String createdJson = mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(privateReq)))
        .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.type").value("PRIVATE"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    UUID privateChannelId = UUID.fromString(objectMapper.readTree(createdJson).get("id").asText());

    // then: u1 기준 findAll 호출하면 public 2개 + private 1개가 포함되어야 함
    mockMvc.perform(get("/api/channels")
            .param("userId", u1.getId().toString())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(3));

    // 추가로: read_status가 실제로 생성됐는지 검증(서비스 로직 확인)
    assertThat(readStatusRepository.findAllByUserId(u1.getId()))
        .anyMatch(rs -> rs.getChannel().getId().equals(privateChannelId));
  }

  @Test
  @Transactional
  void delete_notFound_shouldReturn404() throws Exception {
    UUID notExists = UUID.randomUUID();

    mockMvc.perform(delete("/api/channels/{channelId}", notExists))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").exists())
        .andExpect(jsonPath("$.message").exists());
  }
}
