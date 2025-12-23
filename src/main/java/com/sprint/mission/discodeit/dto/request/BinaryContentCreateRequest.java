package com.sprint.mission.discodeit.dto.request;

public record BinaryContentCreateRequest(
        String fileName, // 이미지 이름
        String contentType, // 전달받았던 헤더에의 타입
        byte[] bytes  // 이미지 정보
) {
}
