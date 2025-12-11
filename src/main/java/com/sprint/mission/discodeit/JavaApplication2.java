package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelReposiory;
import com.sprint.mission.discodeit.repository.file.FileMessageReposiory;
import com.sprint.mission.discodeit.repository.file.FileUserReposiory;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication2 {
    static List<UUID> setupUser(BasicUserService userService) {
         userService.create(new User("황"));
         List<UUID> uerList = userService.getUsers().stream().map(u->u.getId()).toList();
         return uerList;
    }

    static List<Channel> setupChannel(BasicChannelService chService,String chName, List<UUID> inChUsers) {
        chService.create(new Channel(chName,inChUsers));
           return chService.getChannelList();
    }

    static void messageCreateTest(BasicMessageService megService,String megContents , UUID sender,Channel channel) {
        megService.create(new Message(megContents, sender, channel));
    }

    public static void main(String[] args) {
        System.out.println("_______________________미션 2차 심화문제 풀이 __________________________");

        BasicUserService user = new BasicUserService(new FileUserReposiory());
        BasicMessageService meg = new BasicMessageService(new FileMessageReposiory());
        BasicChannelService ch = new BasicChannelService(new FileChannelReposiory());

        List<UUID> users= setupUser(user);
        List<Channel> chList = setupChannel(ch,"1번 채널",users);

        messageCreateTest(meg,"1번 메세지",users.get(0),chList.get(0));

        List<Message> megs= meg.searchByContent(List.of("1번"));
        megs.forEach(m-> System.out.println(m.getMeg()));

        User u = user.findById(meg.getMessages().get(0).getSender());
        System.out.println(u.getUserName());

        user.delete(u.getId());

        List<User> us= user.searchByName(List.of("황"));
        us.forEach(m-> System.out.println(m.getUserName()));

        Channel chch = ch.findById(ch.getChannelList().get(0).getId());
        System.out.println(chch.getChannelName());

        ch.delete(chch.getId());

        List<Channel> chch2= ch.searchByName(List.of("채널"));
        us.forEach(m-> System.out.println(m.getUserName()));
    }
}
