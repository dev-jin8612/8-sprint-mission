package com.sprint.mission.discodeit.service.jwt;

import com.sprint.mission.discodeit.dto.request.JwtInformation;
import com.sprint.mission.discodeit.entity.JwtTokenEntity;
import com.sprint.mission.discodeit.repository.JwtTokenRepository;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class InMemoryJwtRegistry implements JwtRegistry {
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Map<UUID, Queue<JwtInformation>> origin;
    private final int maxActiveJwtCount;

    public InMemoryJwtRegistry(JwtTokenRepository jwtTokenRepository, JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.origin = new ConcurrentHashMap<>();
        this.maxActiveJwtCount = 1;
    }

    @Override
    @Transactional
    public void registerJwtInformation(JwtInformation jwtInformation) {
        UUID userId = jwtInformation.getUser().id();

        origin.putIfAbsent(userId, new ConcurrentLinkedQueue<>());
        Queue<JwtInformation> userQueue = origin.get(userId);

        while (userQueue.size() >= maxActiveJwtCount) {
            JwtInformation oldInfo = userQueue.poll();
            if (oldInfo != null) {
                // In-Memory뿐만 아니라 DB에서도 폐기 처리
                revokeAllInDatabase(oldInfo.getUser().username());
            }
        }

        // 3. 새 로그인 정보 메모리(큐)에 추가
        userQueue.offer(jwtInformation);

        // 3. DB에 새 토큰 저장 (Provider의 toEntity 활용)
        if (jwtInformation.getAccessToken() != null) {
            jwtTokenRepository.save(jwtTokenProvider.toEntity(jwtInformation.getAccessToken()));
        }
        if (jwtInformation.getRefreshToken() != null) {
            jwtTokenRepository.save(jwtTokenProvider.toEntity(jwtInformation.getRefreshToken()));
        }
    }

    @Override
    @Transactional
    public void invalidateJwtInformationByUserId(UUID userId) {// 메모리에서 유저의 큐 자체를 삭제
        Queue<JwtInformation> removedQueue = origin.remove(userId);

        if (removedQueue != null && !removedQueue.isEmpty()) {
            // DB에 있는 해당 유저의 모든 토큰 정보를 폐기
            String username = removedQueue.peek().getUser().username();
            revokeAllInDatabase(username);
        }
    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        Queue<JwtInformation> userQueue = origin.get(userId);
        return userQueue != null && !userQueue.isEmpty();

    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        if (accessToken == null) return false;

        // 전체 큐를 순회하며 해당 Access Token이 존재하는지 확인
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(info -> accessToken.equals(info.getAccessToken()));
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        if (refreshToken == null) return false;

        // 전체 큐를 순회하며 해당 Refresh Token이 존재하는지 확인
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(info -> refreshToken.equals(info.getRefreshToken()));

    }

    @Override
    @Transactional
    public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {
        UUID userId = newJwtInformation.getUser().id();
        Queue<JwtInformation> userQueue = origin.get(userId);

        if (userQueue != null) {
            // 1. 기존 Refresh Token을 가진 정보 삭제
            userQueue.removeIf(info -> refreshToken.equals(info.getRefreshToken()));

            // 2. 갱신된 새로운 토큰 정보 추가
            userQueue.offer(newJwtInformation);

            // 3. DB 로테이션 수행
            // TokenProvider를 이용해 원본 문자열에서 JTI를 추출했다고 가정 (주석 참조)
            String oldJti = jwtTokenProvider.getTokenId(refreshToken);
            String newJti = jwtTokenProvider.getTokenId(newJwtInformation.getRefreshToken());

            jwtTokenRepository.findById(oldJti).ifPresent(t -> {
                t.setRevoked(true);
                t.setReplacedBy(newJti);
                jwtTokenRepository.save(t);
            });

            // 4. 새 토큰 정보를 DB에 저장 (Provider의 toEntity 활용)
            if (newJwtInformation.getAccessToken() != null) {
                jwtTokenRepository.save(jwtTokenProvider.toEntity(newJwtInformation.getAccessToken()));
            }
            if (newJwtInformation.getRefreshToken() != null) {
                jwtTokenRepository.save(jwtTokenProvider.toEntity(newJwtInformation.getRefreshToken()));
            }
        }
    }

    @Override
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void clearExpiredJwtInformation() {
        Date now = new Date();

        // 주기적으로(5분마다) Map을 순회하며 만료된 토큰을 메모리에서 제거
        origin.values().forEach(queue -> {
            queue.removeIf(info -> {
                try {
                    // Provider의 getExpiration 활용하여 현재 시간과 비교
                    boolean isAccessExpired = jwtTokenProvider.getExpiration(info.getAccessToken()).before(now);
                    boolean isRefreshExpired = jwtTokenProvider.getExpiration(info.getRefreshToken()).before(now);

                    // 두 토큰 모두 만료되었다면 큐에서 제거
                    return isAccessExpired && isRefreshExpired;
                } catch (Exception e) {
                    // 파싱 실패(손상된 토큰 등) 시 유효하지 않으므로 제거 대상으로 간주
                    return true;
                }
            });
        });
    }

    private void revokeAllInDatabase(String username) {
        List<JwtTokenEntity> tokens = jwtTokenRepository.findByUsername(username);
        for (JwtTokenEntity t : tokens) {
            t.setRevoked(true);
        }
        jwtTokenRepository.saveAll(tokens);
    }
}
