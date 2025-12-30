package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserReqeust;
import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;
    private final MessageService messageService;
    private final UserService userService;

    @RequestMapping(
            value = "/create",
            method = RequestMethod.GET

    ) public ResponseEntity<ReadStatus>  createReadStatus(
            @RequestParam UUID userId,
            @RequestParam UUID channelId
    ) {
        // 생각해보니 방에 있는 유저인지 확인이랑 중복 생성 안되게 막아야 할거 같은데
        ReadStatus readStatus= readStatusService.create(new ReadStatusCreateRequest(userId, channelId, Instant.now()));
        System.out.println("유저의 수신상태 생성");

        return ResponseEntity.ok(readStatus);
    }

    @RequestMapping(
            value = "/update",
            method = RequestMethod.GET

    ) public ResponseEntity<ReadStatus> updateReadStatus(@RequestParam UUID readStatusId) {
        ReadStatus readStatus = readStatusService.find(readStatusId);
        ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(Instant.now());
        readStatusService.update(readStatusId, request);

        System.out.println("유저의 수신상태 업데이트");

        return ResponseEntity.ok(readStatus);
    }

    @RequestMapping(
            value = "/search",
            method = RequestMethod.GET

    ) public ResponseEntity<ReadStatus>  searchReadStatus(
            @RequestParam String userName,
            @RequestParam UUID channelId
    ) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        Message lastMessage = messages.get(messages.size() - 1);

        UserReqeust user = userService.findByUsername(userName);

        Optional<ReadStatus> readStatus = readStatusService.findAllByUserId(user.id()).stream()
                .filter(readStatus1 -> readStatus1.getChannelId().equals(channelId)).findFirst();

        Instant userReadTime= readStatus.get().getLastReadAt();
        Instant messageCreatTime= lastMessage.getCreatedAt();

        if(userReadTime.isAfter(messageCreatTime)){
            System.out.println(user.username()+"님은 메세지를 봤습니다.");
        }else{
            System.out.println(user.username()+"님은 메세지를 안봤습니다.");
        }

        return  ResponseEntity.ok(readStatus.get());
    }

}
