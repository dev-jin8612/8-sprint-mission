package com.sprint.mission.discodeit.integratedTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @Test
  @Transactional
  void createUser_success_thenFindAll_containsUser() throws Exception {
    // given: JSON 파트 + 파일 파트 (컨트롤러가 multipart/form-data + @RequestPart를 받으므로)
    String json = """
            {
              "username": "alice",
              "email": "alice@test.com",
              "password": "pw1234"
            }
            """;

    MockMultipartFile userCreateRequest = new MockMultipartFile(
        "userCreateRequest",
        "userCreateRequest",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes()
    );

    MockMultipartFile profile = new MockMultipartFile(
        "profile",
        "profile.png",
        MediaType.IMAGE_PNG_VALUE,
        new byte[]{1,2,3,4}
    );

    // when: POST /api/users
    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .file(profile)
            .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        // then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.username", is("alice")))
        .andExpect(jsonPath("$.email", is("alice@test.com")));

    // and: GET /api/users
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", not(empty())))
        .andExpect(jsonPath("$[*].username", hasItem("alice")));
  }

  @Test
  @Transactional
  void createUser_fail_whenDuplicateEmail() throws Exception {
    String json1 = """
            { "username": "alice1", "email": "dup@test.com", "password": "pw1234" }
            """;
    String json2 = """
            { "username": "alice2", "email": "dup@test.com", "password": "pw5678" }
            """;

    MockMultipartFile req1 = new MockMultipartFile("userCreateRequest", "x", MediaType.APPLICATION_JSON_VALUE, json1.getBytes());
    MockMultipartFile req2 = new MockMultipartFile("userCreateRequest", "x", MediaType.APPLICATION_JSON_VALUE, json2.getBytes());

    mockMvc.perform(multipart("/api/users").file(req1))
        .andExpect(status().isCreated());

    mockMvc.perform(multipart("/api/users").file(req2))
        // 전역 예외 처리(ControllerAdvice)에서 어떤 상태코드로 매핑했는지에 따라 조정하세요.
        // 일반적으로 409(CONFLICT)을 많이 씁니다.
        .andExpect(status().isConflict());
  }

  @Test
  @Transactional
  void updateUser_success() throws Exception {
    // 1) 먼저 사용자 생성해서 id 확보
    String createJson = """
            { "username": "bob", "email": "bob@test.com", "password": "pw1234" }
            """;
    MockMultipartFile createReq = new MockMultipartFile("userCreateRequest", "x", MediaType.APPLICATION_JSON_VALUE, createJson.getBytes());

    String createdBody = mockMvc.perform(multipart("/api/users").file(createReq))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    UUID userId = UUID.fromString(objectMapper.readTree(createdBody).get("id").asText());

    // 2) PATCH /api/users/{userId} 는 multipart + PATCH 이므로 builder 사용
    String updateJson = """
            { "newUsername": "bob2", "newEmail": "bob2@test.com", "newPassword": "pw9999" }
            """;
    MockMultipartFile updateReq = new MockMultipartFile("userUpdateRequest", "x", MediaType.APPLICATION_JSON_VALUE, updateJson.getBytes());

    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(updateReq)
            .with(r -> { r.setMethod("PATCH"); return r; })
            .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is("bob2")))
        .andExpect(jsonPath("$.email", is("bob2@test.com")));
  }

  @Test
  @Transactional
  void deleteUser_success_thenNotFound() throws Exception {
    String createJson = """
            { "username": "charlie", "email": "charlie@test.com", "password": "pw1234" }
            """;
    MockMultipartFile createReq = new MockMultipartFile("userCreateRequest", "x", MediaType.APPLICATION_JSON_VALUE, createJson.getBytes());

    String createdBody = mockMvc.perform(multipart("/api/users").file(createReq))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    UUID userId = UUID.fromString(objectMapper.readTree(createdBody).get("id").asText());

    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());

    // delete는 exists 체크 후 삭제하므로, 다시 delete하면 NotFound 기대(전역 예외 매핑에 따라 상태 조정)
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound());
  }
}
