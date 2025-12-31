package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/find")
  public ResponseEntity<BinaryContent> find(@RequestParam UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);

    log.info(binaryContent.getFileName() + " 검색까지는 성공");
    return ResponseEntity.ok(binaryContent);
  }

  @GetMapping("/multiSerach")
  public ResponseEntity<List<BinaryContent>> binaryMulitSerach(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {

    List<BinaryContent> binaryContent = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContent);
  }
}
