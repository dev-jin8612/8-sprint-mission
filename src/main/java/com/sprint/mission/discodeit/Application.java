//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.file.FileChannelReposiory;
//import com.sprint.mission.discodeit.repository.file.FileMessageReposiory;
//import com.sprint.mission.discodeit.repository.file.FileUserReposiory;
//
//import java.util.UUID;
//
//public class Application {
//
//    public static void main(String[] args) {
//        System.out.println("_______________________미션 2차 데이터 영속화__________________________");
//        FileUserReposiory fileUser = new FileUserReposiory();
//        UUID firstUserId = fileUser.getUsers().get(0).getId();
//        UUID secondUserId = fileUser.getUsers().get(1).getId();
//
////        User testUser1 = new User("황");
////        User testUser2 = new User("진");
////        User testUser3 = new User("서");
////        fileUser.create(testUser1);
////        fileUser.create(testUser2);
////        fileUser.create(testUser3);
//
//        // 수정
////        fileUser.update(firstUserId,"서");
//
//        // 유저들 찾기
////        List<User> userList = fileUser.searchByName(List.of("서", "진"));
//        // 단일로 넣어도 동작됨
////        List<User> userList = fileUser.searchByName(List.of("뉴"));
////
////        if (userList != null && userList.size() > 0) {
////            userList.forEach(user -> {
////                System.out.println(user.getUserName());
////            });
////        } else {
////            System.out.println("존재하지 않습니다.");
////        }
//
//        // ID로 찾기
////        User resultUser = fileUser.findById(firstUserId);
//
////        if (resultUser != null) {
////            System.out.println("존재합니다.");
////        }else{
////            System.out.println("존재하지 않습니다.");
////        }
//
//        // 삭제
////        fileUser.delete(firstUserId);
//
//        // ID로 찾기
////        resultUser = fileUser.findById(firstUserId);
//
////        if (resultUser != null) {
////            System.out.println("삭제 안됐습니다.");
////        }else{
////            System.out.println("삭제 되었습니다.");
////        }
//
//
//
////        System.out.println("_______________채팅방_____________________");
//        FileChannelReposiory cTest=new FileChannelReposiory();
//
//        // 참가자 목록
////        List<UUID> usersList = fileUser.getUsers().stream().map(u->u.getId()).toList();
//
//        // 채널 생성
////        Channel cTest1 = new Channel("ctest1", usersList);
////        Channel cTest2 = new Channel("ctest2", usersList);
//
//        // 채널 추가
////        cTest.create(cTest1);
////        cTest.create(cTest2);
//
//        Channel firstCh = cTest.getChannelList().get(0);
////        Channel secondCh = cTest.getChannelList().get(1);
//
//        // 채널 이름 검색
////        List<Channel> chLIst= cTest.searchByName(List.of("ctest1"));
////        chLIst.forEach(u-> System.out.println(u.getChannelName()));
//
//        // 채널 수정
////        cTest.update(firstCh.getId(),"ctest10");
//        // 채널 id 검색
////        Channel ch = cTest.findById(firstCh.getId());
////        System.out.println(ch.getChannelName());
//
////        cTest.delete(firstCh.getId());
////        Channel ch = cTest.findById(firstCh.getId());
////
////        if(ch==null){
////            System.out.println("삭제되었습니다.");
////        }else{
////            System.out.println("삭제되지 않았습니다.");
////        }
//
//
////        System.out.println("_________________메세지__________________");
//        FileMessageReposiory filemeg = new FileMessageReposiory();
//
//        Message mtest1 = new Message("황의 메세지1", firstUserId,firstCh);
//        Message mtest2 = new Message("황의 메세지2", firstUserId,firstCh);
//
////        // 추가
//        filemeg.create(mtest1);
//        filemeg.create(mtest2);
//
//        Message firstMeg = filemeg.getMessages().get(0);
//
//        // 검색, 여러 단어
////        List<Message> megs= filemeg.searchByContent(List.of("황","1"));
////        megs.forEach(m->{
////            System.out.println(m.getMeg());
////        });
//
//        // 메세지 수정
////        filemeg.update(firstMeg.getId(), "서의 메세지1");
//
//        // 메세지 id 찾기
////        System.out.println(filemeg.findById(firstMeg.getId()).getMeg());
//
//        // 메세지 삭제
////        filemeg.delete(firstMeg.getId());
//
////        if(filemeg.findById(firstMeg.getId())==null){
////            System.out.println("삭제되었습니다.");
////        }else{
////            System.out.println("삭제되지 않았습니다.");
////        }
//    }
//}
