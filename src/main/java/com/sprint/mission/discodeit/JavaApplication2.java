package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelService;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageService;
import com.sprint.mission.discodeit.repository.jcf.JCFUserService;

import java.util.List;

public class JavaApplication2 {
    static List<User> setupUser(BasicUserService userService) {
         userService.addUser(new User("황"));
         return userService.getUsers();
    }

    static List<Channel> setupChannel(BasicChannelService chService,String chName, List<User> inChUsers) {
        chService.addChannel(new Channel(chName,inChUsers));
           return chService.getChannelList();
    }

    static void messageCreateTest(BasicMessageService megService,String megContents , User sender,Channel channel) {
        megService.addMessage(new Message(megContents, sender.getId(), channel));
    }

    public static void main(String[] args) {
        System.out.println("_______________________미션 2차 심화문제 풀이 __________________________");

        BasicUserService user = new BasicUserService(new JCFUserService());
        BasicMessageService meg = new BasicMessageService(new JCFMessageService());
        BasicChannelService ch = new BasicChannelService(new JCFChannelService());

        List<User> users= setupUser(user);
        List<Channel> chList = setupChannel(ch,"1번 채널",users);
        messageCreateTest(meg,"1번 메세지",users.get(0),chList.get(0));
        meg.searchMessage("1번");

        user.searchUser("황");
        user.deleteUser(users.get(0).getId());
        user.searchUser("황");

        ch.searchChannel("1번 채널");
        ch.deleteChannel(chList.get(0).getId());
        ch.searchChannel("1번 채널");
    }
}
