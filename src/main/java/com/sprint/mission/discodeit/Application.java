package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Application {
    public static void main(String[] args) {
        FileUserService fileUser = new FileUserService();
        UUID firstUserId = fileUser.getUsers().get(0).getId();
        UUID secondUserId = fileUser.getUsers().get(1).getId();

//        User testUser1 = new User("황");
//        User testUser2 = new User("진");
//        User testUser3 = new User("서");
//        fileUser.addUser(testUser1);
//        fileUser.addUser(testUser2);
//        fileUser.addUser(testUser3);


        // 수정
//        fileUser.updateUser(firstUserId,"서");

        // 찾기
//        fileUser.searchUser("서");


        // 유저들 찾기
//        fileUser.searchUserS(List.of("서","진"));

        // 수정된 찾기
//        fileUser.searchUpdateUser();

        // 삭제
//        fileUser.deleteUser(fileUser.getUsers().get(0).getId());
        // 삭제 확인
//        fileUser.searchUser("황");


//        System.out.println("_______________채팅방_____________________");
        FileChannelService cTest=new FileChannelService();
        Channel firstCh = cTest.getChannels().get(0);
        Channel secondCh = cTest.getChannels().get(1);

//        Channel cTest1 = new Channel("ctest1", fileUser.getUsers());
//        Channel cTest2 = new Channel("ctest2", fileUser.getUsers());

//        cTest.searchChannel("ctest1");
//
//        cTest.addChannel(cTest1);
//        cTest.addChannel(cTest2);
//        cTest.searchChannel("ctest1");
//        cTest.searchChannelS(List.of("ctest1", "ctest2"));

//        cTest.updateChannel(firstCh.getId(),"ctest10");
//        cTest.searchChannel("ctest10");
//        cTest.searchChannel("ctest1");
//        cTest.searchUpdateChannel();

//        cTest.deleteChannel(cTest1.getId());
//        cTest.searchChannel("ctest10");

        System.out.println("_________________메세지__________________");
        FileMessageService filemeg = new FileMessageService();

//        Message mtest1 = new Message("황의 메세지1", firstUserId,firstCh);
//        Message mtest2 = new Message("황의 메세지2", firstUserId,secondCh);

        // 추가
//        filemeg.addMessage(mtest1);
//        filemeg.addMessage(mtest2);
//
//        // 검색, 여러 단어
        filemeg.searchMessageS(List.of("황","1"));
//
//        // 검색, 단일
        filemeg.searchMessage("2");
//
//        filemeg.updateMessage(mtest1.getId(), "서의 메세지1");
//        filemeg.searchMessage("서");
//        filemeg.searchMessage("황");
//
//        // 수정됐던 메세지 출력
//        filemeg.searchUpdateMessage();
//
//        // 메세지 삭제
//        filemeg.deleteMessage(mtest1.getId());
//        filemeg.searchMessage("서");

    }
}
