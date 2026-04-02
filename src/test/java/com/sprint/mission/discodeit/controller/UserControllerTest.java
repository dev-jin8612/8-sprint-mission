package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser
    @DisplayName("사용자 생성 성공 테스트")
    void createUser_Success() throws Exception {
        // Given
        UserCreateRequest createRequest = new UserCreateRequest(
                "testuser",
                "test@example.com",
                "Password1!",
                Role.USER
        );

        MockMultipartFile userCreateRequestPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createRequest)
        );

        MockMultipartFile profilePart = new MockMultipartFile(
                "profile",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test-image".getBytes()
        );

        UUID userId = UUID.randomUUID();
        BinaryContentDTO profileDTO = new BinaryContentDTO(
                UUID.randomUUID(),
                "profile.jpg",
                12L,
                MediaType.IMAGE_JPEG_VALUE
        );

        UserDTO createdUser = new UserDTO(
                userId,
                "testuser",
                "test@example.com",
                Role.USER,
                profileDTO,
                false
        );

        given(userService.create(any(UserCreateRequest.class), any(Optional.class)))
                .willReturn(createdUser);

        // When & Then
        mockMvc.perform(multipart("/api/users")
                        .file(userCreateRequestPart)
                        .file(profilePart)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.profile.fileName").value("profile.jpg"))
                .andExpect(jsonPath("$.online").value(false));
    }

    @Test
    @WithMockUser
    @DisplayName("사용자 생성 실패 테스트 - 유효하지 않은 요청")
    void createUser_Failure_InvalidRequest() throws Exception {
        // Given
        UserCreateRequest invalidRequest = new UserCreateRequest(
                "t",
                "invalid-email",
                "short",
                Role.USER
        );

        MockMultipartFile userCreateRequestPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(invalidRequest)
        );

        // When & Then
        mockMvc.perform(multipart("/api/users")
                        .file(userCreateRequestPart)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("사용자 조회 성공 테스트")
    void findAllUsers_Success() throws Exception {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        UserDTO user1 = new UserDTO(
                userId1,
                "user1",
                "user1@example.com",
                Role.USER,
                null,
                true
        );

        UserDTO user2 = new UserDTO(
                userId2,
                "user2",
                "user2@example.com",
                Role.USER,
                null,
                false
        );

        List<UserDTO> users = List.of(user1, user2);

        given(userService.findAll()).willReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId1.toString()))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].online").value(true))
                .andExpect(jsonPath("$[1].id").value(userId2.toString()))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].online").value(false));
    }
}