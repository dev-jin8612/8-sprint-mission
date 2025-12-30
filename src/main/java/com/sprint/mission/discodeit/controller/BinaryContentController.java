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

@Controller
@ResponseBody
@RequestMapping("/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;
    private final UserService userService;

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> find(@RequestParam UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        System.out.println(binaryContent.getFileName() + " 검색까지는 성공");

        return ResponseEntity.ok(binaryContent);
    }

    @RequestMapping(value = "/multiSerach", method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> binaryMulitSerach(@RequestParam("names") String[] names) {
        List<BinaryContent> binaryContent = new ArrayList<>();
        UserReqeust user;

        for (String s : names) {
            user = userService.findByUsername(s);
            binaryContent.add(binaryContentService.find(user.profileId()));
            System.out.println(user.username() + " 검색까지는 성공");
        }

        return ResponseEntity.ok(binaryContent);
    }
}
