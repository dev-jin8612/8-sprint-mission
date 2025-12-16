//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileChannelReposiory;
//import com.sprint.mission.discodeit.repository.file.FileMessageReposiory;
//import com.sprint.mission.discodeit.repository.file.FileUserReposiory;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//
//import java.util.List;
//import java.util.UUID;
//
//public class JavaApplication {
//    static User setupUser(BasicUserService userService) {
//        return userService.create("황", "1544", "hwang@mail.com");
//    }
//
//    static Channel setupChannel(BasicChannelService chService, String chName, List<UUID> inChUsers, String type) {
//        return chService.create("1번", inChUsers, type);
//    }
//
//    static Message messageCreateTest(BasicMessageService megService, String megContents, UUID sender, Channel channel) {
//        return megService.create(megContents, channel, sender);
//    }
//
//    public static void main(String[] args) {
//        System.out.println("_________________구분선, 유저__________________");
//        FileUserReposiory notUse = new FileUserReposiory();
//        BasicUserService test = new BasicUserService(notUse);
//
//        // 등록
//        User testUser1 = test.create(new User("황"));
//
//        // 수정
//        test.update(testUser1.getId(), "서");
//
//        // 찾기
//        List<User> uList = test.searchByName(List.of("서", "진"));
//        uList.forEach(user -> System.out.println(user.getName()));
//
//        // 삭제
//        test.delete(testUser1.getId());
//
//        // 삭제 확인
//        User checkU = test.findById(testUser1.getId());
//
//        if (checkU != null) {
//            System.out.println("삭제 안됐습니다.");
//        } else {
//            System.out.println("삭제 되었습니다.");
//        }
//
//        System.out.println("_________________구분선, 채널__________________");
//        List<UUID> uerList = List.of(testUser1.getId(), testUser2.getId());
//
//        // 심화 풀이
//        FileChannelReposiory notUse2 = new FileChannelReposiory();
//        BasicChannelService cTest =new BasicChannelService(notUse2);
//
//        Channel cTest1 = cTest.create(new Channel("ctest1", uerList));
//        Channel cTest2 = cTest.create(new Channel("ctest2", uerList));
//
//        // 수정
//        cTest.update(cTest1.getId(), "서", uerList);
//
//        // 찾기
//        List<Channel> cList = cTest.searchByName(List.of("ctest1", "ctest2"));
//
//        cList.forEach(channel -> {
//            System.out.println(channel.getName());
//        });
//
//        // 삭제
//        cTest.delete(cTest1.getId());
//
//        // 삭제 확인
//        Channel checkC = cTest.findById(cTest1.getId());
//        if (checkC != null) {
//            System.out.println("삭제 안됐습니다.");
//        } else {
//            System.out.println("삭제 되었습니다.");
//        }
//
//        System.out.println("_________________구분선, 메세지__________________");
//        FileMessageReposiory notUse3 = new FileMessageReposiory();
//        BasicMessageService megTest = new BasicMessageService(notUse3);
//
//        // 추가
//        Message m1 = megTest.create("황의 메세지1", cTest2, testUser1.getId());
//        Message m2 = megTest.create("황의 메세지2", cTest2, testUser1.getId());
//        Message m3 = megTest.create("진의 메세지2", cTest2, testUser2.getId());
//
//        // 찾기
//        megTest.searchByContent(List.of("진", "1"));
//
//        List<Message> MList = megTest.searchByContent(List.of("ctest1", "ctest2"));
//        MList.forEach(m -> {
//            System.out.println(m.getMeg());
//        });
//
//        // 삭제
//        megTest.delete(m1.getId());
//
//        // 삭제 확인
//        Message checkM = megTest.findById(m1.getId());
//        if (checkC != null) {
//            System.out.println("삭제 안됐습니다.");
//        } else {
//            System.out.println("삭제 되었습니다.");
//        }
//    }
//}
