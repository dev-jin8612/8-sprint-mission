package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.JwtTokenEntity;
import com.sprint.mission.discodeit.repository.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
// TODO 여기도 전체적으로 추가 수정 필요
public class JwtSessionRegistry {
    private final JwtTokenRepository jwtTokenRepository;

    public void register(JwtTokenEntity token) {
        jwtTokenRepository.save(token);
    }

    public void revokeAllByUsername(String username) {
        List<JwtTokenEntity> tokens = jwtTokenRepository.findByUsername(username);
        for (JwtTokenEntity t : tokens) {
            t.setRevoked(true);
        }
        jwtTokenRepository.saveAll(tokens);
    }

    @Transactional(readOnly = true)
    public boolean isRevoked(String jti) {
        boolean result = jwtTokenRepository.findById(jti)
                .map(JwtTokenEntity::isRevoked)
                .orElse(false);
        return result;
    }

    public void markReplaced(String oldJti, String newJti) {
        jwtTokenRepository.findById(oldJti).ifPresent(t -> {
            t.setRevoked(true);
            t.setReplacedBy(newJti);
            jwtTokenRepository.save(t);
        });
    }

    public void revokeByJti(String jti) {
        jwtTokenRepository.findById(jti).ifPresent(t -> {
            t.setRevoked(true);
            jwtTokenRepository.save(t);
        });
    }
}