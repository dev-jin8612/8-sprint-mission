package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends CrudRepository<Notification, UUID> {
    List<Notification> findAllByReceiverId(UUID receiverId);
    Optional<Notification> findById(UUID id);
}