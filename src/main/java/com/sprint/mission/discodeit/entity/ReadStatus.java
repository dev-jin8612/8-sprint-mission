package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "read_statuses", schema = "discodeit")
@NoArgsConstructor
public class ReadStatus {

  @Id
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  //
  private UUID userId;
  private UUID channelId;
  private LocalDateTime lastReadAt;

  public ReadStatus(UUID userId, UUID channelId, LocalDateTime lastReadAt) {
    this.id = UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
    //
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = lastReadAt;
  }

  public void update(LocalDateTime newLastReadAt) {
    boolean anyValueUpdated = false;
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = LocalDateTime.now();
    }
  }
}
