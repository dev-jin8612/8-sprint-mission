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
//        test.updateUser(testUser1.getId(),"서");

        // 찾기
//        test.searchUser("서");

        // 유저들 찾기
//        test.searchUserS(List.of("서","진"));

        // 수정된 찾기
//        test.searchUpdateUser();

        // 삭제
//        test.deleteUser(testUser1.getId());
        // 삭제 확인
//        test.searchUser("서");

//        System.out.println("_________________구분선, 채널__________________");
//        Channel cTest1=new Channel("ctest1",test.getUsers());
//        Channel cTest2=new Channel("ctest2",test.getUsers());
//
//        JCFChannelService cTest=new JCFChannelService();
//        cTest.searchChannel("ctest1");

//        cTest.addChannel(cTest1);
//        cTest.addChannel(cTest2);
//        cTest.searchChannel("ctest1");
//        cTest.searchChannelS(List.of("ctest1", "ctest2"));

//        cTest.updateChannel(cTest1.getId(),"ctest10");
//        cTest.searchChannel("ctest10");
//        cTest.searchChannel("ctest1");
//
//        cTest.deleteChannel(cTest1.getId());
//        cTest.searchChannel("ctest10");

//        System.out.println("_________________구분선, 메세지__________________");
//

        Channel cTest3=new Channel("ctest2",List.of(testUser1));
        Channel cTest4=new Channel("ctest2",List.of(testUser2));

        Message mtest1 = new Message("황의 메세지1", testUser1.getId(),cTest3);
        Message mtest3 = new Message("진의 메세지1", testUser2.getId(),cTest4);
        Message mtest4 = new Message("진의 메세지2", testUser2.getId(),cTest4);
        Message mtest2 = new Message("황의 메세지2", testUser1.getId(),cTest4);
//
//
//        JCFMessageService megTest = new JCFMessageService();
//
//        // 추가
//        megTest.addMessage(mtest1);
//        megTest.addMessage(mtest2);
//        megTest.addMessage(mtest3);
//        megTest.addMessage(mtest4);
//
//        // 검색, 여러 단어
//        megTest.searchMessageS(List.of("진","1"));
//
//        // 검색, 단일
//        megTest.searchMessage("2");
//
//        megTest.updateMessage(mtest1.getId(), "서의 메세지1");
//        megTest.searchMessage("서");
//        megTest.searchMessage("황");
//
//        // 수정됐던 메세지 출력
//        megTest.searchUpdateMessage();
//
//        // 메세지 삭제
//        megTest.deleteMessage(mtest1.getId());
//        megTest.searchMessage("서");

//        System.out.println("_________________구분선, filo__________________");

    }
}
