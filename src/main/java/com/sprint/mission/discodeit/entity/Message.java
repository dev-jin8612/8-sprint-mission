package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "messages", schema = "discodeit")
@NoArgsConstructor
public class Message {

  @Id
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String content;
  private UUID channelId;
  private UUID authorId;

  @Transient // DB에는 저장 안되게, 이거 중간 테이블 쓰면 필요 없기는 할텐데
  private List<UUID> attachmentIds;

  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
    //
    this.content = content;
    this.channelId = channelId;
    this.authorId = authorId;

    // 여기도 삭제 해야할거 같은데
    this.attachmentIds = attachmentIds;
  }

  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = LocalDateTime.now();
    }
  }
}
