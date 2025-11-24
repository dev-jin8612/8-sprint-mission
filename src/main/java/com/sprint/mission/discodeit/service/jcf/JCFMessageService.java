package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    final List<Message> messages = new ArrayList<Message>();

    // 메세지 추가
    @Override
    public void addMessage(Message message) {
        messages.add(message);
    }

    // 메세지 수정
    @Override
    public void updateMessage(UUID mesUid, String contents) {
        Message found = searchU(mesUid);

        if (found != null) {
// 공백 관해서 좀 더 제약 있어야 할 듯
            if (contents != "") {
                System.out.println(found.getMeg() + " -> " + contents);
                found.update(contents);
            } else {

                System.out.println("입력이 없습니다.");
            }

        } else {
            System.out.println("없는 메세지 잆니다.");
        }

    }

    // 메세지 삭제
    @Override
    public void deleteMessage(UUID mesUId) {
        Message meg = searchU(mesUId);

        if (meg != null) {
            System.out.println(meg.getMeg() + " 메세지를 삭제했습니다.");
            messages.remove(meg);
        } else {
            System.out.println("없거나 삭제된 메세지입니다.");
        }
    }

    // 메세지 찾아서 객체 list넘기기
//    @Override
//    public List<Message> searchL(String contents) {
//        List<Message> meg = new ArrayList<>();

    /// /        이거는 여러명 검색을 위한 수정도 필요할 듯
    /// /        아무래도 단체용은 따로 하는게 좋을것 같다.
//        messages.stream().filter(u ->{
//            if (u.getMeg().contains(contents)){
//                meg.add(u);
//            }
//        });
//
//        return meg;
//    }

    // 메세지 찾아서 단일객체 넘기기
    public Message searchU(UUID mesUId) {

        return messages.stream().filter(u -> u.getId().equals(mesUId)).findFirst().orElse(null);
    }

    @Override
    public void searchMessage(UUID mesUId) {
        Message meg = searchU(mesUId);
        if (meg != null) {
            System.out.println(meg.getMeg() + "를 찾았습니다.");

        } else {
            System.out.println("삭제 됐거나 없는 메세지 입니다.");
        }
    }

    // 메세지'들' 찾기
    @Override
    public void searchMessageS(String contents) {
        // 메세지 내용을 검색하게 만들기
        messages.forEach(meg -> {
            if (meg.getMeg().contains(contents)) {
                System.out.println(meg.getMeg());
            }
        });
    }

    // 업데이트가 된적 있는 메세지 탐색
    @Override
    public void searchUpdateMessage() {
        AtomicBoolean notNull = new AtomicBoolean(false);

        messages.forEach(u -> {
            if (u.getCreated() != u.getUpdated()) {
//                이거 uuid로 메세지 가져오고 내용 출력하게 만들어야 할 듯
                System.out.println("업데이트된 메세지: " + u.getMeg());
                notNull.set(true);
            }
        });

        if (notNull.get() == false) {
            System.out.println("업데이트된 메세지가 없습니다.");
        }
    }
}