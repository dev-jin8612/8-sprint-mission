package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    @WithMockUser
    @DisplayName("CSRF 토큰 발급 테스트 - 성공")
    void getCsrfToken_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/auth/csrf-token"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("현재 사용자 정보 조회 테스트 - 성공 (인증됨)")
    void getCurrentUser_Success() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(
                userId,
                "testuser",
                "test@example.com",
                Role.USER,
                null,
                true);

        given(authService.getCurrentUserInfo(any(UserDetails.class))).willReturn(userDTO);

        // When & Then
        mockMvc.perform(get("/api/auth/me").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("현재 사용자 정보 조회 테스트 - 실패 (비인증 상태)")
    void getCurrentUser_Failure_Unauthenticated() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/auth/me").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("사용자 권한 변경 테스트 - 성공")
    void updateRole_Success() throws Exception {
        // Given
        UUID targetUserId = UUID.randomUUID();
        UserRoleUpdateRequest request = new UserRoleUpdateRequest(targetUserId, Role.CHANNEL_MANAGER);
        UserDTO updatedUserDTO = new UserDTO(
                targetUserId,
                "targetuser",
                "target@example.com",
                Role.CHANNEL_MANAGER,
                null, true);

        given(authService.updateRole(any(UserRoleUpdateRequest.class))).willReturn(updatedUserDTO);
        String requestBody = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(put("/api/auth/role")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(targetUserId.toString()))
                .andExpect(jsonPath("$.role").value("CHANNEL_MANAGER"));
    }
}