package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("_________________구분선, 유저__________________");
        JCFUserService test = new JCFUserService();

        // 등록
        User testUser1 = test.create(new User("황"));
        User testUser2 = test.create(new User("진"));

        // 수정
        test.update(testUser1.getId(), "서");

        // 찾기
        List<User> uList = test.searchByName(List.of("서", "진"));
        uList.forEach(user -> System.out.println(user.getUserName()));

        // 삭제
        test.delete(testUser1.getId());

        // 삭제 확인
        User checkU = test.findById(testUser1.getId());

        if (checkU != null) {
            System.out.println("삭제 안됐습니다.");
        } else {
            System.out.println("삭제 되었습니다.");
        }

        System.out.println("_________________구분선, 채널__________________");

        List<UUID> uerList = List.of(testUser1.getId(), testUser2.getId());

        JCFChannelService cTest = new JCFChannelService();

        Channel cTest1 = cTest.create(new Channel("ctest1", uerList));
        Channel cTest2 = cTest.create(new Channel("ctest2", uerList));

        // 수정
        cTest.update(cTest1.getId(), "서", uerList);

        // 찾기
        List<Channel> cList = cTest.searchByName(List.of("ctest1", "ctest2"));

        cList.forEach(channel -> {
            System.out.println(channel.getChannelName());
        });

        // 삭제
        cTest.delete(cTest1.getId());

        // 삭제 확인
        Channel checkC = cTest.findById(cTest1.getId());
        if (checkC != null) {
            System.out.println("삭제 안됐습니다.");
        } else {
            System.out.println("삭제 되었습니다.");
        }

        System.out.println("_________________구분선, 메세지__________________");

        JCFMessageService megTest = new JCFMessageService();

        // 추가
        Message m1 = megTest.create("황의 메세지1", cTest2, testUser1.getId());
        Message m2 = megTest.create("황의 메세지2", cTest2, testUser1.getId());
        Message m3 = megTest.create("진의 메세지2", cTest2, testUser2.getId());

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
        if (checkC != null) {
            System.out.println("삭제 안됐습니다.");
        } else {
            System.out.println("삭제 되었습니다.");
        }
    }
}
