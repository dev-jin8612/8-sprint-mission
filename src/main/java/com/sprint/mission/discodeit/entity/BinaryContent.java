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
@Table(name = "binary_contents", schema = "discodeit")
@NoArgsConstructor
public class BinaryContent {

  @Id
  private UUID id;

  private LocalDateTime createdAt;
  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
    //
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
