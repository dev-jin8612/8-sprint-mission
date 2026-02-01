package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.LocalBinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
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
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final LocalBinaryContentStorage binaryContentStorage;

  @Override
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDTO> find(@PathVariable UUID binaryContentId) {
    BinaryContent bc = binaryContentService.find(binaryContentId);
    log.info("{} 검색까지는 성공", bc.getFileName());

    return ResponseEntity.ok(
        new BinaryContentDTO(
            bc.getId(),
            bc.getFileName(),
            bc.getSize(),
            bc.getContentType()
        )
    );
  }

  @Override
  @GetMapping
  public ResponseEntity<List<BinaryContentDTO>> binaryMultiSearch(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds
  ) {
    List<BinaryContentDTO> result =
        binaryContentService.findAllByIdIn(binaryContentIds).stream()
            .map(bc -> new BinaryContentDTO(
                bc.getId(),
                bc.getFileName(),
                bc.getSize(),
                bc.getContentType()
            ))
            .toList();

    return ResponseEntity.ok(result);
  }

  @Override
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> download(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);

    return binaryContentStorage.download(
        new BinaryContentDTO(
            binaryContent.getId(),
            binaryContent.getFileName(),
            binaryContent.getSize(),
            binaryContent.getContentType()
        )
    );
  }
}
