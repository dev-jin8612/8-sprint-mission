package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "users", schema = "discodeit")
public class User extends BaseUpdatetableEntity {
//  전체 참고

  @Column(name = "username", nullable = false, unique = true, length = 50)
  private String username;

  @Column(name = "password", nullable = false, length = 60)
  private String password;

  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

  @OneToOne(mappedBy = "user",
      cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus userStatus;

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "profile_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_users_binary_content"))
  private BinaryContent profile;

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  public User(String username, String password, String email, BinaryContent profile) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.profile = profile;
  }

  public void update(String username, String password, String email, BinaryContent profile) {
    if (username != null) {
      this.username = username;
    }
    if (email != null) {
      this.email = email;
    }
    if (password != null) {
      this.password = password;
    }
    if (profile != null) {
      this.profile = profile;
    }
  }

  public void setUserStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
    if (userStatus.getUser() != this) {
      userStatus.setUser(this);
    }
  }

}
