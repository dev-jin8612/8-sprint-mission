package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import java.time.Duration;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "binary_contents", schema = "discodeit")
@NoArgsConstructor
public class UserStatus {

    @Id
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //
    private UUID userId;
    private LocalDateTime lastActiveAt;

    public UserStatus(UUID userId, LocalDateTime lastActiveAt) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        //
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public void update(LocalDateTime lastActiveAt) {
        boolean anyValueUpdated = false;

        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public Boolean isOnline() {
        LocalDateTime instantFiveMinutesAgo = LocalDateTime.now().minus(Duration.ofMinutes(5));

        return lastActiveAt.isAfter(instantFiveMinutesAgo);
    }
}
