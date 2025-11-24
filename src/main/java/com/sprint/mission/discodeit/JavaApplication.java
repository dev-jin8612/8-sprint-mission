package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
//        System.out.println("_________________구분선, 유저__________________");
        JCFUserService test = new JCFUserService();

        // 등록
        User testUser1 = new User("황");
        User testUser2 = new User("진");
        test.addUser(testUser1);
        test.addUser(testUser2);

        // 수정
//        test.updateUser("황","서");
//        test.updateUser("진","서서");

        // 찾기
//        test.searchUser("서");

        // 유저들 찾기
//        test.searchUserS(List.of("서","진"));

        // 수정된 찾기
//        test.searchUpdateUser();
        // 수정된 유저들 찾기
//        test.searchUpdateUserS(List.of("서","서서"));

        // 삭제
//        test.deleteUser("황");
        // 삭제 확인
//        test.searchUser("황");

//        System.out.println("_________________구분선, 채널__________________");

//        Channel cTest1=new Channel("test1",List.of(testUser1));
//        Channel cTest1=new Channel("test1", list);

//        JCFChannelService cTest=new JCFChannelService(List.of(cTest1));

        System.out.println("_________________구분선, 메세지__________________");

        Message mtest1 = new Message("황의 메세지1", testUser1.getId());
        Message mtest2 = new Message("황의 메세지2", testUser1.getId());
        Message mtest3 = new Message("진의 메세지1", testUser2.getId());
        Message mtest4 = new Message("진의 메세지2", testUser2.getId());


        JCFMessageService megTest = new JCFMessageService();

        // 추가
        megTest.addMessage(mtest1);
        megTest.addMessage(mtest2);
        megTest.addMessage(mtest3);
        megTest.addMessage(mtest4);

        // 검색, 단어 포함하는
        megTest.searchMessageS("진");

        // 검색, 단일, 추후 수정 필요
        megTest.searchMessage(mtest1.getId());

        megTest.updateMessage(mtest1.getId(), "서의 메세지1");
        megTest.searchMessage(mtest1.getId());

        // 수정됐던 메세지 출력
        megTest.searchUpdateMessage();

        // 메세지 삭제
        megTest.deleteMessage(mtest1.getId());
        megTest.searchMessage(mtest1.getId());
    }
}
