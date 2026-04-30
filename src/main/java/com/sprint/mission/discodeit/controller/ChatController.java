package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChatMessageDto;
import com.sprint.mission.discodeit.entity.MessageType;
import com.sprint.mission.discodeit.service.ChatService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;


/**
 * WebSocket 기반 실시간 채팅 메시지를 처리하는 컨트롤러
 * STOMP 프로토콜을 통한 메시지 송수신과 채팅방 관리 기능을 담당한다
 * 
 * 주요 기능:
 * - 실시간 채팅 메시지 송수신 처리
 * - 사용자 입장/퇴장 알림 처리
 * - 채팅 메시지 데이터베이스 저장
 * - SSE 알림 서비스와 연동
 */
@Controller
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

//    private final ChatService chatService;					// 채팅 메시지 처리 서비스
    private final MessageService messageService;
    private final NotificationService notificationService;	// SSE 알림 서비스

    public ChatController(MessageService messageService, NotificationService notificationService) {
        this.messageService = messageService;
        this.notificationService = notificationService;
    }
    /**
     * 채팅 메시지를 처리하고 브로드캐스트한다
     * 클라이언트에서 /app/chat.sendMessage/{roomId}로 전송된 메시지를 처리한다
     * 
     * @param roomId 채팅방 ID
     * @param chatMessage 전송된 채팅 메시지
     * @return 처리된 채팅 메시지 (구독자들에게 브로드캐스트됨)
     */
    @MessageMapping("/chat.sendMessage/{roomId}")
	/* 설명. @SendTo: 메서드의 반환값을 지정된 목적지로 브로드캐스트하는 어노테이션이다.
	 *  해당 채팅방(/topic/room/{roomId})을 구독하고 있는 모든 클라이언트에게 메시지를 전송한다.
	 * */
    @SendTo("/topic/room/{roomId}")
    public ChatMessageDto sendMessage(
            /* 설명. @DestinationVariable: 메시지 목적지 경로의 변수를 메서드 파라미터로 받는 어노테이션이다.
             *  @MessageMapping의 경로에서 {roomId}와 같은 경로 변수를 추출하여 매개변수로 전달한다.
			 *  기존 HTTP 요청에서 사용하던 @PathVariable 어노테이션과 동일한 역할을 한다고 이해하면 된다.
             * */
            @DestinationVariable String roomId,
            /* 설명. @Payload: WebSocket 메시지의 본문을 메서드 파라미터로 받는 어노테이션이다.
             *  클라이언트가 전송한 JSON 메시지를 자동으로 객체로 변환하여 메서드에 전달한다.
			 *  기존 HTTP 요청에서 사용하던 @RequestBody 어노테이션과 동일한 역할을 한다고 이해하면 된다.
             * */
            @Payload ChatMessageDto chatMessage,
            /* 설명. Principal: WebSocketInterceptor.handleConnect()에서 StompPrincipal로 주입된 사용자 식별 정보이다.
             *  클라이언트가 CONNECT 프레임에 실은 userId 헤더를 서버가 세션에 바인딩하여 전달한다.
             *  페이로드의 sender 값과 대조하여 위·변조를 방지하는 기본적 인증 개념을 시연한다.
             * */
            Principal principal) {

        // 1. Principal 기반 sender 보정 (페이로드 sender와 인증 주체가 다르면 인증 주체로 강제)
        reconcileSender(chatMessage, principal);

        log.info("[채팅 메시지] 메시지 처리 시작 - roomId: {}, sender: {}", roomId, chatMessage.getSender());

        try {
            // 2. 메시지 유효성 검증
            if (chatMessage == null || chatMessage.getContent() == null || chatMessage.getContent().trim().isEmpty()) {
                log.warn("[채팅 메시지] 유효하지 않은 메시지 - roomId: {}", roomId);
                return null;
            }

            // 3. 메시지 타입이 지정되지 않은 경우 기본값 설정
            if (chatMessage.getType() == null) {
                chatMessage.setType(MessageType.CHAT);
            }

            // 4. 채팅 메시지 데이터베이스 저장
            ChatMessageDto savedMessage = messageService.saveMessage(roomId, chatMessage);
            log.debug("[채팅 메시지] 메시지 저장 완료 - messageId: {}", savedMessage.getMessageId());
            
            // 5. SSE를 통한 실시간 알림 전송 (비동기)
            try {
                notificationService.notifyNewMessage(roomId, savedMessage);
                log.debug("[채팅 메시지] SSE 알림 전송 완료 - roomId: {}", roomId);
            } catch (Exception e) {
                log.error("[채팅 메시지] SSE 알림 전송 실패 - roomId: {}, error: {}", roomId, e.getMessage());
                // SSE 알림 실패는 채팅 메시지 처리에 영향을 주지 않도록 한다
            }
            
            log.info("[채팅 메시지] 메시지 처리 완료 - messageId: {}, roomId: {}", savedMessage.getMessageId(), roomId);
            return savedMessage;
            
        } catch (Exception e) {
            log.error("[채팅 메시지] 메시지 처리 중 오류 발생 - roomId: {}, error: {}", roomId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 사용자 채팅방 입장을 처리하고 시스템 메시지를 브로드캐스트한다
     * 
     * @param roomId 입장할 채팅방 ID
     * @param chatMessage 입장 메시지 정보
     * @return 입장 알림 메시지 (구독자들에게 브로드캐스트됨)
     */
    @MessageMapping("/chat.addUser/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public ChatMessageDto addUser(
            @DestinationVariable String roomId,
            @Payload ChatMessageDto chatMessage,
            Principal principal) {

        // 1. Principal 기반 sender 보정
        reconcileSender(chatMessage, principal);

        String username = chatMessage.getSender();
        log.info("[사용자 입장] 채팅방 입장 처리 시작 - roomId: {}, user: {}", roomId, username);

        try {
            // 2. 입장 메시지 생성
            chatMessage.setType(MessageType.JOIN);
            chatMessage.setContent(username + "님이 채팅방에 입장했습니다.");

            // 3. 시스템 메시지로 데이터베이스 저장
            ChatMessageDto savedMessage = messageService.saveMessage(roomId, chatMessage);
            log.debug("[사용자 입장] 입장 메시지 저장 완료 - messageId: {}", savedMessage.getMessageId());

            // 4. 사용자 온라인 상태 업데이트
            try {
                messageService.updateUserOnlineStatus(username, true);
                log.debug("[사용자 입장] 사용자 온라인 상태 업데이트 완료 - user: {}", username);
            } catch (Exception e) {
                log.warn("[사용자 입장] 사용자 상태 업데이트 실패 - user: {}, error: {}", username, e.getMessage());
            }

            // 5. SSE 알림 채팅방 구독 등록 (이후 해당 방 메시지 알림을 수신할 수 있도록)
            try {
                notificationService.subscribeToRoom(username, roomId);
                log.debug("[사용자 입장] SSE 채팅방 구독 등록 완료 - user: {}, roomId: {}", username, roomId);
            } catch (Exception e) {
                log.warn("[사용자 입장] SSE 채팅방 구독 실패 - user: {}, roomId: {}, error: {}", username, roomId, e.getMessage());
            }

            log.info("[사용자 입장] 채팅방 입장 처리 완료 - roomId: {}, user: {}", roomId, username);
            return savedMessage;

        } catch (Exception e) {
            log.error("[사용자 입장] 입장 처리 중 오류 발생 - roomId: {}, user: {}, error: {}",
                     roomId, username, e.getMessage(), e);

            // 오류 발생 시에도 기본 입장 메시지는 전송
            chatMessage.setType(MessageType.JOIN);
            chatMessage.setContent(username + "님이 채팅방에 입장했습니다.");
            return chatMessage;
        }
    }

    /**
     * 사용자 채팅방 퇴장을 처리하고 시스템 메시지를 브로드캐스트한다
     * 
     * @param roomId 퇴장할 채팅방 ID
     * @param chatMessage 퇴장 메시지 정보
     * @return 퇴장 알림 메시지 (구독자들에게 브로드캐스트됨)
     */
    @MessageMapping("/chat.removeUser/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public ChatMessageDto removeUser(
            @DestinationVariable String roomId,
            @Payload ChatMessageDto chatMessage,
            Principal principal) {

        // 1. Principal 기반 sender 보정
        reconcileSender(chatMessage, principal);

        String username = chatMessage.getSender();
        log.info("[사용자 퇴장] 채팅방 퇴장 처리 시작 - roomId: {}, user: {}", roomId, username);

        try {
            // 2. 퇴장 메시지 생성
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setContent(username + "님이 채팅방에서 나갔습니다.");

            // 3. 시스템 메시지로 데이터베이스 저장
            ChatMessageDto savedMessage = messageService.saveMessage(roomId, chatMessage);
            log.debug("[사용자 퇴장] 퇴장 메시지 저장 완료 - messageId: {}", savedMessage.getMessageId());

            // 4. 사용자 오프라인 상태 업데이트
            try {
                messageService.updateUserOnlineStatus(username, false);
                log.debug("[사용자 퇴장] 사용자 오프라인 상태 업데이트 완료 - user: {}", username);
            } catch (Exception e) {
                log.warn("[사용자 퇴장] 사용자 상태 업데이트 실패 - user: {}, error: {}", username, e.getMessage());
            }

            // 5. SSE 알림 채팅방 구독 해제 (해당 방 메시지 알림을 더 이상 수신하지 않도록)
            try {
                notificationService.unsubscribeFromRoom(username, roomId);
                log.debug("[사용자 퇴장] SSE 채팅방 구독 해제 완료 - user: {}, roomId: {}", username, roomId);
            } catch (Exception e) {
                log.warn("[사용자 퇴장] SSE 채팅방 구독 해제 실패 - user: {}, roomId: {}, error: {}", username, roomId, e.getMessage());
            }

            log.info("[사용자 퇴장] 채팅방 퇴장 처리 완료 - roomId: {}, user: {}", roomId, username);
            return savedMessage;

        } catch (Exception e) {
            log.error("[사용자 퇴장] 퇴장 처리 중 오류 발생 - roomId: {}, user: {}, error: {}",
                     roomId, username, e.getMessage(), e);

            // 오류 발생 시에도 기본 퇴장 메시지는 전송
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setContent(username + "님이 채팅방에서 나갔습니다.");
            return chatMessage;
        }
    }

    /**
     * Principal과 페이로드의 sender 일치성을 검증하고, 불일치 시 Principal 기준으로 보정한다.
     * 기본적인 위·변조 방지 개념 시연을 위한 로직이며, Principal이 없으면 페이로드 값을 그대로 사용한다.
     *
     * @param chatMessage 수신된 메시지
     * @param principal   STOMP 세션의 Principal (WebSocketInterceptor에서 주입)
     */
    private void reconcileSender(ChatMessageDto chatMessage, Principal principal) {
        if (chatMessage == null || principal == null) {
            return;
        }

        String authenticatedUserId = principal.getName();
        String payloadSender = chatMessage.getSender();

        if (payloadSender == null || !payloadSender.equals(authenticatedUserId)) {
            log.warn("[보안] sender 불일치 감지 - payload: {}, principal: {} → principal 기준으로 강제 보정",
                     payloadSender, authenticatedUserId);
            chatMessage.setSender(authenticatedUserId);
        }
    }
}