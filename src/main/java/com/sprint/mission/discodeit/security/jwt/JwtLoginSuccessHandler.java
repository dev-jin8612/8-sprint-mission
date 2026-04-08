package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.response.JwtDTO;
import com.sprint.mission.discodeit.entity.JwtTokenEntity;
import com.sprint.mission.discodeit.service.user.DiscodeitUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider tokenProvider;
    private final JwtSessionRegistry jwtSessionRegistry;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 응답 인코딩/콘텐츠 타입 설정
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Principal 유효성 확인 및 캐스팅
        if (authentication.getPrincipal() instanceof DiscodeitUserDetails customUserDetails) {
            try {
                // 1. 동일 계정 기존 토큰 전부 무효화(동시 로그인 제한)
                jwtSessionRegistry.revokeAllByUsername(customUserDetails.getUsername());

                // 2. 새 Access/Refresh 발급
                String accessToken = tokenProvider.generateAccessToken(customUserDetails);
                String refreshToken = tokenProvider.generateRefreshToken(customUserDetails);

                // 3. 토큰 메타데이터 저장 (toEntity로 중복 제거)
                JwtTokenEntity accessEntity = tokenProvider.toEntity(accessToken);
                JwtTokenEntity refreshEntity = tokenProvider.toEntity(refreshToken);
                jwtSessionRegistry.register(accessEntity);
                jwtSessionRegistry.register(refreshEntity);

                // 4. 리프레시 쿠키 설정
                tokenProvider.addRefreshCookie(response, refreshToken);

                // 사용자 DTO 구성
                UserDto userDto = customUserDetails.getUserDto();

                // 5. JwtDto 바디 전송
                JwtDTO jwtDto = new JwtDTO(userDto, accessToken);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(jwtDto));

            } catch (Exception e) {
                // 예외 발생 시 처리(500)
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(objectMapper.createObjectNode()
                        .put("success", false)
                        .put("message", "Token generation failed")
                        .toString());
            }
        } else {
            // 인증 실패 시 처리(401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.createObjectNode()
                    .put("success", false)
                    .put("message", "Invalid principal")
                    .toString());
        }
    }
}