package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Slf4j
@Configuration
public class CacheConfig {
    // TODO 여기 미션 대로 수정 좀 하기
    @Bean
    public CacheManager compositeCacheManager() {
        CompositeCacheManager composite = new CompositeCacheManager();
        composite.setCacheManagers(List.of(
                categoryCacheManager(),       // Category 캐시
                menuCacheManager(),           // Menu 캐시
                securityCacheManager()        // Security 보안 캐시
        ));
        composite.setFallbackToNoOpCache(false);
        return composite;
    }

    @Bean
    public CacheManager categoryCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)                         // 카테고리는 적으므로 500개 제한
                .expireAfterWrite(Duration.ofMinutes(30)) // 카테고리 변경이 드물어 30분 유지
                .recordStats()                            // 메트릭 수집 활성화
                .removalListener((key, value, cause) -> { /* 로깅 */ })
        );

        manager.setCacheNames(List.of("categories", "categoryByCode"));
        return manager;
    }

    @Bean
    public CacheManager menuCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.setCaffeine(Caffeine.newBuilder()
                // 메모리 관리 설정
                .maximumSize(1000)
                // 최대 1000개의 Entry까지 메모리에 보관
                // LFU + LRU 혼합 알고리즘 사용

                // 시간 기반 만료 정책 설정
                .expireAfterWrite(Duration.ofMinutes(10))
                // 캐시 엔트리 쓰기 후 10분 경과 시 자동 만료

                .expireAfterAccess(Duration.ofMinutes(5))
                // 캐시 엔트리 마지막 접근 후 5분 경과 시 자동 만료

                // 성능 및 모니터링 설정
                .recordStats()
                // 캐시 성능 통계 수집 활성화
                // Spring Boot Actuator와 연동하여 메트릭 조회 가능

                // 캐시 제거 이벤트 리스너
                .removalListener((key, value, cause) -> {
                    switch (cause) {
                        // SIZE: 크기 초과로 인한 제거 (정상적인 LRU/LFU 동작)
                        case SIZE:
                            log.debug("캐시 크기 초과로 인한 엔트리 제거 - key: {}", key);
                            break;
                        // EXPIRED: 시간 만료로 인한 제거 (TTL 정책)
                        case EXPIRED:
                            log.debug("만료 시간 도달로 인한 엔트리 제거 - key: {}", key);
                            break;
                        // EXPLICIT: 수동 삭제 (@CacheEvict 등)
                        case EXPLICIT:
                            log.info("수동 삭제로 인한 엔트리 제거 - key: {}", key);
                            break;
                        // REPLACED: 새 값으로 교체
                        case REPLACED:
                            log.debug("새 값으로 교체로 인한 엔트리 제거 - key: {}", key);
                            break;
                        // 그 외 제거 원인
                        default:
                            log.debug("캐시 엔트리 제거 - key: {}, cause: {}", key, cause);
                    }
                })
        );

        manager.setCacheNames(List.of("menus", "menuByCode"));
        return manager;
    }

    @Bean
    public CacheManager securityCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        // 보안 최적화된 Caffeine 캐시 정책 설정
        manager.setCaffeine(Caffeine.newBuilder()
                // 최대 500명의 동시 사용자 정보 캐시
                .maximumSize(500)

                // 쓰기 후 2분 만료: 사용자 정보 변경 시 빠른 반영
                .expireAfterWrite(Duration.ofMinutes(2))

                // 접근 후 1분 만료: 비활성 사용자 정보 빠른 정리
                .expireAfterAccess(Duration.ofMinutes(1))

                // 통계 수집 활성화: 보안 감사 및 성능 모니터링
                .recordStats()

                // 캐시 제거 시 보안 로그 기록
                .removalListener((key, value, cause) -> {
                    log.debug("보안 캐시 엔트리 제거 - username: {}, 제거 원인: {}", key, cause);
                })
        );

        manager.setCacheNames(List.of("userDetails"));
        return manager;
    }
}
