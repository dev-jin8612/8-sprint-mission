package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        User testUser1=new User("황");
        JCFUserService test = new JCFUserService(List.of(testUser1));

        // 유저 등록
        User testUser2=new User("진");
        test.addUser(testUser2);

        // 유저 수정
//        test.updateUser("황","서");
//        test.updateUser("진","서서");

        // 유저 찾기, 없을 경우 이상해지는거 해결 ispa..
//        test.searchUser("서");

        // 유저들 찾기
//        test.searchUserS(List.of("서","진"));

        // 수정된 유저 찾기
//        test.searchUpdateUser();
        // 수정된 유저들 찾기
//        test.searchUpdateUserS(List.of("서","서서"));

        // 유저 삭제
//        test.deleteUser("황");
        // 삭제 확인
//        test.searchUser("황");

//        __________________________________________

        Channel cTest1=new Channel("test1",List.of(testUser1));

        JCFChannelService cTest=new JCFChannelService(List.of(cTest1));























    }
}
