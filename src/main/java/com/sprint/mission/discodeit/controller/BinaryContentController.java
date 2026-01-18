package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.LocalBinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/binaryContents")
@Tag(name = "Binary API", description = "Binary 관련 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final LocalBinaryContentStorage binaryContentStorage;

  @GetMapping("/find/{binaryContentId}")
  @Operation(summary = "Binary 단건 조회", description = "Binary 하나를 조회합니다.")
  public ResponseEntity<BinaryContent> find(
      @Parameter(name = "binaryContentId", description = "조회할 Binary의 UUID입니다.", example = "/binaryContents/550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);

    log.info(binaryContent.getFileName() + " 검색까지는 성공");
    return ResponseEntity.ok(binaryContent);
  }

  @GetMapping
  @Operation(summary = "Binary 다건 조회", description = "Binary를 여러개 조회합니다.")
  public ResponseEntity<List<BinaryContent>> binaryMulitSerach(
      @Parameter(name = "List<UUID> binaryContentIds", description = "조회할 BinaryId를 연속으로 입력합니다. ,로 구분 합니다.", example = "/550e8400-e29b-41d4-a716-446655440000, 550e8400-e29b-41d4-a716-446655440000", required = true) @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {

    List<BinaryContent> binaryContent = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContent);
  }

  @GetMapping("/{binaryContentId}/download")
  @Operation(summary = "Binary 다운로드", description = "메세지에 있는 이미지를 다운로드하게 해줍니다.")
  public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);

    return binaryContentStorage.download(
        new BinaryContentDTO(
            binaryContent.getId(), binaryContent.getFileName(),
            binaryContent.getSize(), binaryContent.getContentType()
        ));
  }

}
