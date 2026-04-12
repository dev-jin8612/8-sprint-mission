package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtDTO {
    private UserDto user;
    private String accessToken;

    @Override
    public String toString() {
        return "JwtDTO{" + "user=" + user + ", accessToken='" + accessToken + '\'' + '}';
    }
}