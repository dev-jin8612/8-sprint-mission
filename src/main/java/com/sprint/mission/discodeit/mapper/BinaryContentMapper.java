package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.io.IOException;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {
  BinaryContentDTO toDto(BinaryContent binaryContent);

  default BinaryContentCreateRequest toCreateRequest(MultipartFile upload) {
    if (upload == null) {
      return null;
    }
    try {
      return new BinaryContentCreateRequest(
          upload.getOriginalFilename(),
          upload.getContentType(),
          upload.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException("파일 변환 중 오류 발생", e);
    }
  }
}
