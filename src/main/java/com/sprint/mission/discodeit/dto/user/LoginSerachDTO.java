package com.sprint.mission.discodeit.dto.user;

public record LoginSerachDTO(
        String name,
        String password
) {
    public LoginSerachDTO {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있어요.");
        }
        if (password == null) {
            throw new IllegalArgumentException("비밀번호가 없어요.");
        }
    }
}
