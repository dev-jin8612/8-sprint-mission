package com.sprint.mission.discodeit.service.jwt;

import com.sprint.mission.discodeit.dto.request.JwtInformation;

import java.util.UUID;

public interface JwtRegistry {
    void registerJwtInformation(JwtInformation jwtInformation);

    void invalidateJwtInformationByUserid(UUID userld);

    void hasActiveJwtInformationByUserid(UUID userld);

    void hasActiveJwtInformationByAccessToken(String accessToken);

    void hasActiveJwtInformationByRefreshToken(String refreshToken);

    void rotateJwtInformation(String refreshToken,String  newJwtInformation);

    void clearExpiredJwtInformation();
}
