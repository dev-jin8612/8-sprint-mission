package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "channels")
public class Channel extends BaseUpdatetableEntity {

  // ORDINAL: enum이 수정되면 서수가 꼬임.
  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private ChannelType type;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;


// 내가 못넣었던거
  @OneToMany(mappedBy = "channel", orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Message> messages =  new ArrayList<>();

  @OneToMany(mappedBy = "channel", orphanRemoval = true, fetch = FetchType.LAZY)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }
}
