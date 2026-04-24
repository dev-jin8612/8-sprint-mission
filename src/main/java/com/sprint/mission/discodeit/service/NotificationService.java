package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotifications(String username);
}
