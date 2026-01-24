package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseUpdatetableEntity extends BaseEntity {

  @LastModifiedDate
  @Column(name = "updated_at")
  protected Instant updatedAt;
}
