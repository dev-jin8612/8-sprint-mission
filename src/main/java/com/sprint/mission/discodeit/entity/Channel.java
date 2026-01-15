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
@Table(name = "channels", schema = "discodeit")
@NoArgsConstructor
public class Channel {

  @Id
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  //
  private ChannelType type;
  private String name;
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
    //
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      anyValueUpdated = true;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = LocalDateTime.now();
    }
  }
}
