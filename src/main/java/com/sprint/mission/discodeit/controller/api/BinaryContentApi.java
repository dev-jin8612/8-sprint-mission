package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Binary API", description = "Binary 관련 API")
public interface BinaryContentApi {

  @Operation(summary = "Binary 단건 조회", description = "Binary 하나를 조회합니다.")
  ResponseEntity<BinaryContentDTO> find(
      @Parameter(
          name = "binaryContentId",
          description = "조회할 Binary의 UUID입니다.",
          example = "550e8400-e29b-41d4-a716-446655440000",
          required = true
      )
      @PathVariable UUID binaryContentId
  );

  @Operation(summary = "Binary 다건 조회", description = "Binary를 여러개 조회합니다.")
  ResponseEntity<List<BinaryContentDTO>> binaryMultiSearch(
      @Parameter(
          description = "조회할 BinaryId를 ,로 구분하여 입력합니다.",
          example = "550e8400-e29b-41d4-a716-446655440000,550e8400-e29b-41d4-a716-446655440001",
          required = true
      )
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds
  );

  @Operation(summary = "Binary 다운로드", description = "Binary 파일을 다운로드합니다.")
  ResponseEntity<Resource> download(
      @Parameter(
          description = "다운로드할 Binary의 UUID입니다.",
          required = true
      )
      @PathVariable UUID binaryContentId
  );
}
