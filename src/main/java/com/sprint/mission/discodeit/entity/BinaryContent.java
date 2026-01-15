package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "binary_contents", schema = "discodeit")
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContent {

  @Id
  private UUID id;

  private LocalDateTime createdAt;
  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;
}
