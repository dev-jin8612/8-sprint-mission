package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelReqeust;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final ReadStatusService readStatusService;
    private final UserService userService;

    // 채널 생성
    @RequestMapping(
            value = "/create/{channelType}",
            method = RequestMethod.GET

    ) public ResponseEntity<Channel> createChannel(
            @RequestParam String[] name, // private면 참여자들의 이름, public이면 방 이름 입력
            @RequestParam(required = false) String description, //private면 필요없으니 false로
            // public와 private의 name이 서로 타입이 달라
            // 적당히 편집해 사용하기 위해서 각 dto는 사용하지 않았습니다.
            @PathVariable ChannelType channelType) {

        Channel channel;
        ChannelReqeust channelReqeust;

        if (name == null) throw new IllegalArgumentException("Channel name is null");

        if (channelType.equals(ChannelType.PRIVATE)) {
            List<UUID> participantIds = new ArrayList<>();
            for (String username : name) participantIds.add(userService.findByUsername(username).id());
            channel= channelService.create(new PrivateChannelCreateRequest(participantIds));
            System.out.println("비공개 채널 생성까지는 성공");

        } else {
            channel = channelService.create(new PublicChannelCreateRequest(name[0], description));
            System.out.println(channel.getName() + "채널 생성까지는 성공");
        }

        return ResponseEntity.ok(channel);
    }

    // 채널 수정
    @RequestMapping(
            value = "/update/{channelName}",
            method = RequestMethod.GET

    ) public  ResponseEntity<ChannelReqeust> updateChannel(
            @PathVariable String channelName,
            PublicChannelUpdateRequest request
    ) {
        ChannelReqeust channel = channelService.findByName(channelName);
        channelService.update(channel.id(), request);
        System.out.println(channel.name() + "에서 " + request.newName() + " 수정까지는 성공");

        return ResponseEntity.ok(channel);
    }

    // 검색 조회
    @RequestMapping(
            value = "/channelSearch",
            method = RequestMethod.GET

    ) public ResponseEntity<List<List<ChannelReqeust>>>  channelSearch(@RequestParam String[] names) {
            List<List<ChannelReqeust>> channelReqeustList = new ArrayList<>() ;
        for (String name : names) {
            List<ChannelReqeust> channelReqeust  = channelService.findAllByUserId(
                    userService.findByUsername(name).id());

            channelReqeustList.add(channelReqeust);

            System.out.println(name + "님이 있는 채널은 " + channelReqeust.toString() + "입니다.");
        }

        return ResponseEntity.ok(channelReqeustList);
    }

    // 채널 삭제
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void deleteChannel(
            @RequestParam(required = false) String channelName,
            @RequestParam(required = false) UUID channelId

    ) {ChannelReqeust channelReqeust;

        if (channelId != null) {
            System.out.println("channel ID로 삭제 시도");
            channelReqeust = channelService.find(channelId);

        } else if (channelName != null && !channelName.isEmpty()) {
            System.out.println(channelName + " 삭제 시도");
            channelReqeust = channelService.findByName(channelName);

        } else throw new IllegalArgumentException("입력이 잘못 되었습니다.");

        channelService.delete(channelReqeust.id());
        System.out.println("삭제까지는 성공");
    }
}
