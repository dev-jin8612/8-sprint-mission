package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {
    private static final Logger log = LoggerFactory.getLogger(MessageWebSocketController.class);
    private final MessageService messageService;
    private final NotificationService notificationService;	// SSE 알림 서비스

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public MessageDto sendMessage(
            @DestinationVariable String roomId,
            @Payload MessageDto chatMessage,
            Principal principal) {

        // 1. Principal 기반 sender 보정 (페이로드 sender와 인증 주체가 다르면 인증 주체로 강제)
        reconcileSender(chatMessage, principal);

        try {
            // 2. 메시지 유효성 검증
            if (chatMessage == null || chatMessage.content() == null || chatMessage.content().trim().isEmpty()) {
                log.warn("[채팅 메시지] 유효하지 않은 메시지 - roomId: {}", roomId);
                return null;
            }
 
            // 4. 채팅 메시지 데이터베이스 저장
            MessageDto savedMessage = messageService.create(roomId, chatMessage);

            // 5. SSE를 통한 실시간 알림 전송 (비동기)
            try {
                notificationService.create(roomId, savedMessage);
                log.debug("[채팅 메시지] SSE 알림 전송 완료 - roomId: {}", roomId);
            } catch (Exception e) {
                log.error("[채팅 메시지] SSE 알림 전송 실패 - roomId: {}, error: {}", roomId, e.getMessage());
                // SSE 알림 실패는 채팅 메시지 처리에 영향을 주지 않도록 한다
            }
            
            log.info("[채팅 메시지] 메시지 처리 완료 - messageId: {}, roomId: {}", savedMessage.id(), roomId);
            return savedMessage;
            
        } catch (Exception e) {
            log.error("[채팅 메시지] 메시지 처리 중 오류 발생 - roomId: {}, error: {}", roomId, e.getMessage(), e);
            return null;
        }
    }

    @MessageMapping("/chat.addUser/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public MessageDto addUser(
            @DestinationVariable String roomId,
            @Payload MessageDto chatMessage,
            Principal principal) {

        // 1. Principal 기반 sender 보정
        reconcileSender(chatMessage, principal);
        String username = chatMessage.author().username();

        try {
            // 2. 입장 메시지 생성
            chatMessage.setContent(username + "님이 채팅방에 입장했습니다.");

            // 3. 시스템 메시지로 데이터베이스 저장
            MessageDto savedMessage = messageService.create(roomId, chatMessage);
            log.debug("[사용자 입장] 입장 메시지 저장 완료 - messageId: {}", savedMessage.id());

            // 4. 사용자 온라인 상태 업데이트
            try {
                messageService.update(username, true);
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
            chatMessage.setContent(username + "님이 채팅방에 입장했습니다.");
            return chatMessage;
        }
    }

    @MessageMapping("/chat.removeUser/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public MessageDto removeUser(
            @DestinationVariable String roomId,
            @Payload MessageDto chatMessage,
            Principal principal) {

        // 1. Principal 기반 sender 보정
        reconcileSender(chatMessage, principal);
        String username = chatMessage.getSender();

        try {
            // 2. 퇴장 메시지 생성
            chatMessage.setContent(username + "님이 채팅방에서 나갔습니다.");

            // 3. 시스템 메시지로 데이터베이스 저장
            MessageDto savedMessage = messageService.create(roomId, chatMessage);
            log.debug("[사용자 퇴장] 퇴장 메시지 저장 완료 - messageId: {}", savedMessage.id());

            // 4. 사용자 오프라인 상태 업데이트
            try {
                messageService.update(username, false);
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
            chatMessage.setContent(username + "님이 채팅방에서 나갔습니다.");
            return chatMessage;
        }
    }

    private void reconcileSender(MessageDto chatMessage, Principal principal) {
        if (chatMessage == null || principal == null) {
            return;
        }

        String authenticatedUserId = principal.getName();
        String payloadSender = chatMessage.author().username();

        if (payloadSender == null || !payloadSender.equals(authenticatedUserId)) {
            log.warn("[보안] sender 불일치 감지 - payload: {}, principal: {} → principal 기준으로 강제 보정",
                     payloadSender, authenticatedUserId);
            chatMessage.setSender(authenticatedUserId);
        }
    }
}