package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserReqeust;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/binary")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;
    private final UserService userService;

    @RequestMapping(value = "/singleSerach", method = RequestMethod.GET)
    public BinaryContent binarySingleSerach(@RequestParam String name) {
        UserReqeust UserReqeust = userService.findByUsername(name);
        BinaryContent binaryContent = binaryContentService.find(UserReqeust.profileId());
        System.out.println(binaryContent.getFileName() + " 검색까지는 성공");

        return binaryContent;
    }

    @RequestMapping(value = "/multiSerach", method = RequestMethod.GET)
    public void binaryMulitSerach(@RequestParam("names") String[] names) {
        List<UserReqeust> userReqeust = new ArrayList<>();
        List<BinaryContent> binaryContent = new ArrayList<>();
        UserReqeust user;

        for (String s : names) {
            user = userService.findByUsername(s);

            userReqeust.add(user);
            binaryContent.add(binaryContentService.find(user.profileId()));

            System.out.println(user.username() + " 검색까지는 성공");
        }
    }
}
