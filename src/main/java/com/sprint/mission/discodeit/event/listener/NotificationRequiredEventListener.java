package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//@Component
@RequiredArgsConstructor
public class NotificationRequiredEventListener {
    private final NotificationService notificationService;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    @TransactionalEventListener
    public void on(MessageCreatedEvent event) {
        MessageDto messageDto = event.messageDto();

        Message message = messageRepository.findById(messageDto.id())
                .orElseThrow(() -> new MessageNotFoundException());

        UUID channelId = messageDto.channelId();
        ChannelDto channel = channelService.find(channelId);

        Set<UUID> receiverIds = readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(
                        channelId)
                .stream().map(readStatus -> readStatus.getUser().getId())
                .filter(receiverId -> !receiverId.equals(message.getAuthor().getId()))
                .collect(Collectors.toSet());
        String title = message.getAuthor().getUsername()
                .concat(
                        channel.type().equals(ChannelType.PUBLIC) ?
                                String.format(" (#%s)", channel.name()) : ""
                );
        String content = message.getContent();

        notificationService.create(receiverIds, title, content);
    }

    @TransactionalEventListener
    public void on(RoleUpdatedEvent event) {
        String title = "권한이 변경되었습니다.";
        String content = String.format("%s -> %s", event.oldRole(), event.newRole());
        notificationService.create(Set.of(event.userId()), title, content);
    }
}