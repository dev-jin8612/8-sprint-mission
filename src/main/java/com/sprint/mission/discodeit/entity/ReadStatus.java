package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(
    name = "read_statuses",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_read_statuses_user_channel",
        columnNames = {"user_id", "channel_id"}
    ))
public class ReadStatus extends BaseUpdatetableEntity {
  // 여기는 대부분 참조

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_statuses_user"))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_read_statuses_channel"))
  private Channel channel;

  @Column(name = "last_read_at")
  private LocalDateTime lastReadAt;

  public ReadStatus(User user, Channel channel, LocalDateTime lastReadAt) {

    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void update(LocalDateTime newLastReadAt) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
    }
  }
}

