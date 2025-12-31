package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserReqeust;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@ResponseBody
@RequestMapping("/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "/find", method = RequestMethod.GET)
  public ResponseEntity<BinaryContent> find(@RequestParam UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    System.out.println(binaryContent.getFileName() + " 검색까지는 성공");

    return ResponseEntity.ok(binaryContent);
  }

  @RequestMapping(value = "/multiSerach", method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContent>> binaryMulitSerach(@RequestParam UUID[] profileIds) {
    List<BinaryContent> binaryContent = new ArrayList<>();
    BinaryContent img = null;

    for (UUID s : profileIds) {
      img = binaryContentService.find(s);
      binaryContent.add(img);
      System.out.println(img.getFileName() + " 이미지 검색까지는 성공");
    }

    return ResponseEntity.ok(binaryContent);
  }
}
