package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public abstract class BaseUpdatetableEntity extends BaseEntity {
  @LastModifiedDate
  @Column(name = "updated_at")
  protected LocalDateTime updatedAt;
}
