package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "messages", schema = "discodeit")
public class Message extends BaseUpdatetableEntity {
// 여기는 전체적으로 참고함
  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_messages_channel"))
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_messages_author"))
  private User author;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "message_attachments",
//      schema = "discodeit",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachmentIds = new ArrayList<>();

  public Message(String content, Channel channel, User author, List<BinaryContent> attachmentIds) {
    this.content = content;
    this.channel = channel;
    this.author = author;
    this.attachmentIds = attachmentIds;
  }

  public void update(String newContent) {
    if (newContent != null) {
      this.content = newContent;
    }
  }
}
