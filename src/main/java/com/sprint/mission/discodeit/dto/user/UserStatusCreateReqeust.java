package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserStatusCreateReqeust(
        String name,
        String password,
        String email,
        UUID profileId
) {
    /* Interface는 소괄호()에서 끝난다. ex) User create(String name);
    그냥 파라미터만 보낼꺼면 Interface처럼 하면 되는데 왜 중괄호{}가 있을까
    무언가 추가적인 기능을 제공하기 위해서가 아닐까?
    찾아보니 메소드를 정의 가능했다.
    여기서 검증을 해도 되지 않을까해서 만들어 봤다.
    아래 throw는 찾아보면서 나온거라 써봤다.

    생성시에는 비밀번호나 메일이 공백이면 안되기에
    공백을 확인하는 로직으로 만들어 보았다.
    */
    public UserStatusCreateReqeust {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있어요.");
        }
        if (password == null) {
            throw new IllegalArgumentException("비밀번호가 없어요.");
        }
        if (email == null) {
            throw new IllegalArgumentException("메일이 없어요.");
        }
    }
}
