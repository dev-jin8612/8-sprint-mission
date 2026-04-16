package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtInformation {
    private UserDto user;
    private String accessToken;
    private String refreshToken;

    public void rotate(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
