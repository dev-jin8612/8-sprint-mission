package com.sprint.mission.discodeit.security.checker;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component("messageAuthChecker")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageAuthChecker {
    private final MessageRepository messageRepository;

    public boolean isOwner(Authentication authentication, UUID messageId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            return false;
        }

        String currentUsername = authentication.getName();
        return message.getAuthor().getUsername().equals(currentUsername);
    }
}
