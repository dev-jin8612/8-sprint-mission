package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateReqeust;
import com.sprint.mission.discodeit.dto.channel.FindReqeust;
import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateReqeust;
import com.sprint.mission.discodeit.dto.message.MessageBinaryContentReqeust;
import com.sprint.mission.discodeit.dto.user.UserStatusCreateReqeust;
import com.sprint.mission.discodeit.dto.user.UserStatusReqeust;
import com.sprint.mission.discodeit.dto.user.UserStatusFindReqeust;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
    static UserStatusReqeust setupUser(BasicUserService userService) {
        // 반환을 뭐로 해줘야 하나
        return userService.create(
                new UserStatusCreateReqeust("황", "1544", "123@gmail.com", null));
    }

    static Channel setupChannel(BasicChannelService chService, ReadStatusCreateReqeust dto, String type) {
        if (type.equals(ChannelType.PRIVATE.toString())) {
            return chService.createPrivate(dto.memberIds());
        }
        return chService.createPublic(dto);
    }

    static Message messageCreateTest(BasicMessageService megService, MessageBinaryContentReqeust dto) {
        return megService.create(dto);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        System.out.println("_________________구분선, 유저__________________");
        BasicUserService test = context.getBean(BasicUserService.class);

        // 등록
        UserStatusReqeust testUser1 = setupUser(test);

        // {}는 접어서 안보이게 만들기 위해 넣었습니다.
        {
            // 수정
            UserStatusReqeust testUser2 = new UserStatusReqeust(
                    // 수정할 대상은 testUser1이니 id는 그대로
                    testUser1.userid(),
                    "진",
                    "1717",
                    "jinjin@naver.com",
                    null
            );

            test.update(testUser2);

            // 찾기
            List<User> uList = test.searchByName(List.of("서", "진"));
            uList.forEach(user -> System.out.println(user.getName()));

            // 삭제
            test.delete(testUser1.userid());

             // 삭제 확인
            UserStatusFindReqeust checkU = test.findById(testUser1.userid());

            if (checkU != null) {
                System.out.println("삭제 안됐습니다.");
            } else {
                System.out.println("삭제 되었습니다.");
            }
        }

        System.out.println("_________________구분선, 채널__________________");
        List<UUID> uerList = List.of(testUser1.userid());

        BasicChannelService channelTest = context.getBean(BasicChannelService.class);

        ReadStatusCreateReqeust readStatusDto = new ReadStatusCreateReqeust("1번 채널", uerList);
        Channel channelTest1 = setupChannel(channelTest, readStatusDto, "PUBLIC");

        {
            // 수정
            ChannelUpdateReqeust cuDto = new ChannelUpdateReqeust(channelTest1.getId(), "서", ChannelType.PRIVATE);
            System.out.println(channelTest.update(cuDto).getName());
            System.out.println(channelTest.update(cuDto).getType());

            // 찾기
            List<Channel> channelList = channelTest.searchByName(List.of("channelTest1", "channelTest2"));

            channelList.forEach(channel -> {
                System.out.println(channel.getName());
            });

        }
        // 삭제
            channelTest.delete(channelTest1.getId());

        // 삭제 확인
        FindReqeust checkC = channelTest.findById(channelTest1.getId());
        if (checkC != null) {
            System.out.println("삭제 안됐습니다.");
        } else {
            System.out.println("삭제 되었습니다.");
        }


        System.out.println("_________________구분선, 메세지__________________");
        BasicMessageService megTest = context.getBean(BasicMessageService.class);

        // 추가
        MessageBinaryContentReqeust mcDto = new MessageBinaryContentReqeust(null, null, "황의 메세지1", channelTest1, testUser1.userid());
        Message m1 = messageCreateTest(megTest, mcDto);

        {
            // 찾기
            megTest.searchByContent(List.of("진", "1"));

            List<Message> messagelList = megTest.searchByContent(List.of("channelTest1", "channelTest2"));
            messagelList.forEach(m -> {
                System.out.println(m.getMeg());
            });

            // 삭제
            megTest.delete(m1.getId());

            // 삭제 확인
            Message checkM = megTest.findById(m1.getId());
            if (checkM != null) {
                System.out.println("삭제 안됐습니다.");
            } else {
                System.out.println("삭제 되었습니다.");
            }
        }
    }
}