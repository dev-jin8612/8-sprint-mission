package com.sprint.mission.discodeit.entity;

import lombok.Getter;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message {



  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  //
  private String content;
  //
  private UUID channelId;
  private UUID authorId;
  private List<UUID> attachmentIds;

  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
    //
    this.content = content;
    this.channelId = channelId;
    this.authorId = authorId;
    this.attachmentIds = attachmentIds;
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}
