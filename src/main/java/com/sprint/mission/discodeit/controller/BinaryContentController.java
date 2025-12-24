package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
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
        UserDto userdto = userService.findByUsername(name);
        BinaryContent binaryContent = binaryContentService.find(userdto.id());

        System.out.println(binaryContent.getFileName() + " 검색까지는 성공");
    }


    @ResponseBody
    @RequestMapping(value = "/multiSerach", method = RequestMethod.GET)
    public void binaryMulitSerach(@RequestParam String[] name) {
        List<UserDto> userdto = new ArrayList<>();
        List<BinaryContent> binaryContent = new ArrayList<>();

        for (String s : name) {
            userdto.add(userService.findByUsername(s));
        }

        userdto.forEach(userdto1 -> {
            binaryContent.add(binaryContentService.find(userdto1.id()));
        });


        System.out.println(binaryContent.getFileName() + " 검색까지는 성공");
    }
}
