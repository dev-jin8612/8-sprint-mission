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
@Table(name = "users", schema = "discodeit")
@NoArgsConstructor
public class User {

  @Id
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  //
  private String username;
  private String email;
  private String password;
  private UUID profileId;     // BinaryContent

  public User(String username, String email, String password, UUID profileId) {
    this.id = UUID.randomUUID();
    this.createdAt = LocalDateTime.now();
    //
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
    boolean anyValueUpdated = false;
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
      anyValueUpdated = true;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
      anyValueUpdated = true;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
      anyValueUpdated = true;
    }
    if (newProfileId != null && !newProfileId.equals(this.profileId)) {
      this.profileId = newProfileId;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = LocalDateTime.now();
    }
  }
}
