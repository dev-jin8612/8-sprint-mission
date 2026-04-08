package com.sprint.mission.discodeit.service.jwt;

import com.sprint.mission.discodeit.dto.request.JwtInformation;
import com.sprint.mission.discodeit.entity.JwtTokenEntity;
import com.sprint.mission.discodeit.repository.JwtTokenRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InMemoryJwtRegistry implements JwtRegistry {
    private final JwtTokenRepository jwtTokenRepository;
    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
    private final int maxActiveJwtCount;

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        //(JwtTokenEntity token) { 흠... 수정 필요할 듯 TODO
        jwtTokenRepository.save(token);

    }

    @Override
    public void invalidateJwtInformationByUserid(UUID userld) {

//        public void revokeAllByUsername(String username) {
//            List<JwtTokenEntity> tokens = jwtTokenRepository.findByUsername(username);
//            for (JwtTokenEntity t : tokens) {
//                t.setRevoked(true);
//            }
//            jwtTokenRepository.saveAll(tokens);
//        }

        // 위에 꺼 보면서 수정해보기
    }

    @Override
    public void hasActiveJwtInformationByUserid(UUID userld) {

    }

    @Override
    public void hasActiveJwtInformationByAccessToken(String accessToken) {

//        @Transactional(readOnly = true)
//        public boolean isRevoked(String jti) {
//            boolean result = jwtTokenRepository.findById(jti)
//                    .map(JwtTokenEntity::isRevoked)
//                    .orElse(false);
//            return result;
//        }

//        public void revokeByJti(String jti) {
//            jwtTokenRepository.findById(jti).ifPresent(t -> {
//                t.setRevoked(true);
//                jwtTokenRepository.save(t);
//            });
//        }

        // 위에 꺼 보면서 수정해보기
    }

    @Override
    public void hasActiveJwtInformationByRefreshToken(String refreshToken) {

    }

    @Override
    public void rotateJwtInformation(String refreshToken, String newJwtInformation) {

//        public void markReplaced(String oldJti, String newJti) {
//            jwtTokenRepository.findById(oldJti).ifPresent(t -> {
//                t.setRevoked(true);
//                t.setReplacedBy(newJti);
//                jwtTokenRepository.save(t);
//            });
//        }
        // 위에꺼 보면서 수정해보기
    }

    @Override
    @Scheduled(fixedDelay = 1000* 60 * 5)
    public void clearExpiredJwtInformation() {

    }
}
