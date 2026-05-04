package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.NotificationDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> getNotifications(UUID receiverId);
    void deleteNotification(UUID notificationId, String username);
    void create(Set<UUID> receiverIds, String title, String content);
}
