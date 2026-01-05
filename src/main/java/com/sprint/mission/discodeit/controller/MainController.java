package com.sprint.mission.discodeit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Home API", description = "최초 접속시 화면입니다.")
public class MainController {

  @RequestMapping("/")
  @Operation(summary = "메인화면", description = "주소 입력시 보이는 최초의 화면입니다.")
  public String userlist() {
    return "redirect:/user-list.html";
  }
}
