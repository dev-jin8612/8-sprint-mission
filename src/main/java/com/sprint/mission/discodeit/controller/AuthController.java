package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {
    private final AuthService authService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        log.debug("CSRF 토큰 요청: {}", csrfToken.getToken());
        return ResponseEntity.noContent().build(); // 204 Void 응답
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            log.info("비 인증 사용자");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        UserDTO userDTO = authService.getCurrentUserInfo(userDetails);

        if(userDTO ==null){
            log.info("비 인증 사용자");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        log.info("[AuthController] 현재 사용자 정보 조회 완료: "+userDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/role")
    public ResponseEntity<UserDTO> updateRole(
            @RequestBody UserRoleUpdateRequest userRoleUpdateRequest
    ) {
        UserDTO userDTO = authService.updateRole(userRoleUpdateRequest);
        return ResponseEntity.ok(userDTO);
    }
}
