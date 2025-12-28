package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelReqeust;
import com.sprint.mission.discodeit.dto.data.UserReqeust;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ChannelService channelService;
    private final UserService userService;

    @ResponseBody
    @RequestMapping(
            value = "/send",
            method = RequestMethod.GET,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void createMessage(
            @RequestParam String content,
//            @RequestParam String channelName,
            @RequestParam UUID channelId,
            @RequestParam String senderName,
            @RequestParam(required = false) List<MultipartFile> img
    ) throws IOException {
//        ChannelReqeust channelReqeust = channelService.findByName(channelName);
        UserReqeust userReqeust = userService.findByUsername(senderName);
        ChannelReqeust channelReqeust = channelService.find(channelId);
        List<BinaryContentCreateRequest> binaryContentCreateRequest = new ArrayList<>();

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(
                content, channelReqeust.id(),userReqeust.id()
        );

        img.forEach(upload -> {
                    try {
                        binaryContentCreateRequest.add(new BinaryContentCreateRequest(
                                upload.getOriginalFilename(),
                                upload.getContentType(),
                                upload.getBytes()
                        ));
                    }catch (IOException e){
                        throw new RuntimeException(e);
                    }});

        messageService.create(messageCreateRequest,binaryContentCreateRequest);
        System.out.println("메세지 보내기 성공");
    }

    @ResponseBody
    @RequestMapping(
            value = "/update",
            method = RequestMethod.GET
    )
    public void updateMessage(
            @RequestParam UUID messageId,
            @RequestParam MessageUpdateRequest newContent
    ){
        Message message = messageService.update(messageId,newContent);
        System.out.println(message.getContent() + "로 메세지 수정 성공");
    }

    @ResponseBody
    @RequestMapping(value = "/search/{channelId}")
    public void searchMessage(@PathVariable UUID channelId){
         List<Message> message= messageService.findAllByChannelId(channelId);
         message.stream().forEach(message1 -> System.out.println(message1.getContent()));
    }

    @ResponseBody
    @RequestMapping(value = "/delete/{messageId}")
    public void deleteMessage(@PathVariable UUID messageId){
        messageService.delete(messageId);
        System.out.println("메세지 삭제 성공");
    }
}
