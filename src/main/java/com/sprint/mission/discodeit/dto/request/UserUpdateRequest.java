package com.sprint.mission.discodeit.dto.request;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

  public static record UserCreateRequest(
      String email,
      String username,
      String password
  ) {

  }
}
