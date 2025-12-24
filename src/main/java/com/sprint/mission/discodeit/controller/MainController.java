package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/*")
@RequiredArgsConstructor
public class MainController {

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String userlist(LoginRequest loginRequest) {
        return "user-list";
    }
}
