package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserReqeust;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/binary")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;
    private final UserService userService;

    @ResponseBody
    @RequestMapping(value = "/singleSerach", method = RequestMethod.GET)
    public void binarySingleSerach(@RequestParam String name) {
        UserReqeust UserReqeust = userService.findByUsername(name);
        BinaryContent binaryContent = binaryContentService.find(UserReqeust.profileId());

        System.out.println(binaryContent.getFileName() + " 검색까지는 성공");
    }


    @ResponseBody
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
