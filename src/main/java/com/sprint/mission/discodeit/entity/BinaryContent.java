package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "binary_contents", schema = "discodeit")
@NoArgsConstructor
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "size")
  private Long size;

  @Column(name = "content_type")
  private String contentType;

  public BinaryContent(String fileName, Long size, String contentType) {

    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}
