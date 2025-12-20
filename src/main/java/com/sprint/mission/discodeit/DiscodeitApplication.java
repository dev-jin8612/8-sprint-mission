package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ch.ChUpdateDTO;
import com.sprint.mission.discodeit.dto.ch.FindDTO;
import com.sprint.mission.discodeit.dto.ch.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.meg.MegBCDTO;
import com.sprint.mission.discodeit.dto.meg.MegCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserStatusDTO;
import com.sprint.mission.discodeit.dto.user.UserStatusFindDTO;
import com.sprint.mission.discodeit.entity.ChType;
import com.sprint.mission.discodeit.entity.Channel;
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
    static UserStatusDTO setupUser(BasicUserService userService) {
        // 반환을 뭐로 해줘야 하나
        return userService.create(
                new UserStatusCreateDTO("황", "1544", "123@gmail.com", null));
    }

    static Channel setupChannel(BasicChannelService chService, ReadStatusCreateDTO dto, String type) {
        if (type.equals(ChType.PRIVATE.toString())) {
            return chService.createPrivate(dto.memberIds());
        }
        return chService.createPublic(dto);
    }

    static Message messageCreateTest(BasicMessageService megService, MegBCDTO dto) {
        return megService.create(dto);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        System.out.println("_________________구분선, 유저__________________");
        BasicUserService test = context.getBean(BasicUserService.class);

        // 등록
        UserStatusDTO testUser1 = setupUser(test);

        // {}는 접어서 안보이게 만들기 위해 넣었습니다.
        {
            // 수정
            UserStatusDTO testUser2 = new UserStatusDTO(
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
            UserStatusFindDTO checkU = test.findById(testUser1.userid());

            if (checkU != null) {
                System.out.println("삭제 안됐습니다.");
            } else {
                System.out.println("삭제 되었습니다.");
            }
        }

        System.out.println("_________________구분선, 채널__________________");
        List<UUID> uerList = List.of(testUser1.userid());

        BasicChannelService cTest = context.getBean(BasicChannelService.class);

        ReadStatusCreateDTO rscDto = new ReadStatusCreateDTO("1번 채널", uerList);
        Channel cTest1 = setupChannel(cTest, rscDto, "PUBLIC");

        {
            // 수정
            ChUpdateDTO cuDto = new ChUpdateDTO(cTest1.getId(), "서", ChType.PUBLIC);
            System.out.println(cTest.update(cuDto).getName());

            // 찾기
            List<Channel> cList = cTest.searchByName(List.of("ctest1", "ctest2"));

            cList.forEach(channel -> {
                System.out.println(channel.getName());
            });

        }
        // 삭제
            cTest.delete(cTest1.getId());

        // 삭제 확인
        FindDTO checkC = cTest.findById(cTest1.getId());
        if (checkC != null) {
            System.out.println("삭제 안됐습니다.");
        } else {
            System.out.println("삭제 되었습니다.");
        }


        System.out.println("_________________구분선, 메세지__________________");
        BasicMessageService megTest = context.getBean(BasicMessageService.class);

        // 추가
        MegBCDTO mcDto = new MegBCDTO(null, null, "황의 메세지1", cTest1, testUser1.userid());
        Message m1 = messageCreateTest(megTest, mcDto);

        {
            // 찾기
            megTest.searchByContent(List.of("진", "1"));

            List<Message> MList = megTest.searchByContent(List.of("ctest1", "ctest2"));
            MList.forEach(m -> {
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