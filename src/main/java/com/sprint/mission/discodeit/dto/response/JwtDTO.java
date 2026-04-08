package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.UserDto;

public class JwtDTO {

    private UserDto user;
    private String accessToken;

    public JwtDTO() {
    }

    public JwtDTO(UserDto user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "JwtDTO{" +
                "user=" + user +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}


