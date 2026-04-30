package com.sprint.mission.discodeit.config.webSocket;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    private final WebSocketInterceptor webSocketInterceptor;

    /**
     * 메시지 브로커 설정을 구성한다
     * STOMP 프로토콜에서 사용할 메시지 라우팅 규칙을 정의한다
     *
     * @param config 메시지 브로커 설정 객체
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 1. Spring이 지원하는 간단한 인메모리 브로커(SimpleBroker) 활성화
        config.enableSimpleBroker(
                // "/topic"으로 시작하는 경로는 브로커가 처리하여 구독자들에게 메시지를 브로드캐스트한다.
                // 예를 들어 "/topic/chat" 경로로 메시지가 전송되면, 모든 구독자에게 메시지가 전송된다.
                "/topic",
                // "/queue"는 개별 사용자를 위한 메시지 대기열이다.
                // 예를 들어 "/queue/chat" 경로로 메시지가 전송되면, 해당 사용자에게만 메시지가 전송된다.
                "/queue"
        );

        // 2. 클라이언트에서 서버로 메시지를 보낼 때 사용할 경로(path) 접두사 설정
        // "/pub"으로 시작하는 경로로 오는 메시지는 @MessageMapping 어노테이션이 정의된 메서드로 라우팅된다
        config.setApplicationDestinationPrefixes("/pub");

        // 3. 개별 사용자를 위한 메시지 경로 접두사 설정
        // "/sub"로 시작하는 경로는 특정 사용자에게만 메시지를 전송할 때 사용한다
        config.setUserDestinationPrefix("/sub");
    }

    /**
     * WebSocket 엔드포인트를 등록한다
     * 클라이언트가 WebSocket 서버에 연결할 때 사용할 경로를 정의한다
     *
     * @param registry STOMP 엔드포인트 등록 객체
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 1. STOMP 엔드포인트로 "/ws" 경로를 등록한다: 클라이언트는 "ws://localhost:8080/chat" 경로로 WebSocket 연결을 시도한다.
        registry.addEndpoint("/ws")
                // 2. CORS 설정 - 모든 오리진에서의 접근을 허용 (개발 환경용)
                .setAllowedOriginPatterns("*")
                // 3. SockJS 폴백 활성화 - WebSocket을 지원하지 않는 브라우저를 위한 대안 제공(클라이언트 측 지식임)
                // SockJS는 WebSocket이 불가능한 환경에서 HTTP 기반 폴링 등의 대안을 제공한다
                .withSockJS()
                // 4. SockJS 설정 - 하트비트 간격과 연결 해제 지연 시간 설정
                .setHeartbeatTime(25000)       // 25초마다 하트비트 전송
                .setDisconnectDelay(5000);   // 5초 후 연결 해제
    }

    /**
     * 클라이언트 인바운드 채널을 설정한다
     * 클라이언트에서 서버로 들어오는 메시지 처리를 위한 스레드 풀을 설정하고 전용 인터셉터를 등록한다
     *
     * @param registration 채널 등록 객체
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 1. 메시지 처리용 스레드 풀 설정
        registration.taskExecutor()
                .corePoolSize(4)       // 기본 스레드 수
                .maxPoolSize(8)         // 최대 스레드 수
                .queueCapacity(100);  // 대기 큐 용량

        // 2. WebSocket 인터셉터 등록 (사용자 인증 및 세션 관리용 bean을 주입받아 등록한다)
        registration.interceptors(webSocketInterceptor);
    }

    /**
     * 클라이언트 아웃바운드 채널을 설정한다
     * 서버에서 클라이언트로 나가는 메시지 전송을 위한 스레드 풀을 설정한다
     *
     * @param registration 채널 등록 객체
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        // 1. 메시지 전송용 스레드 풀 설정
        registration.taskExecutor()
                .corePoolSize(4)       // 기본 스레드 수
                .maxPoolSize(8)         // 최대 스레드 수
                .queueCapacity(100);  // 대기 큐 용량
    }
}