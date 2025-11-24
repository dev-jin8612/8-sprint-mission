package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    final ArrayList<Message> Messages;

    public JCFMessageService(List<Message> Messages) {
        this.Messages = new ArrayList<>(Messages);
    }

    // 메세지 추가
    @Override
    public void addMessage(Message message) {
        Messages.add(message);
    }

    // 메세지 수정
    @Override
    public void updateMessage(UUID mesUid, String contents) {
        Message found = search(mesUid);

        if (found != null) {
// 공백 관해서 좀 더 제약 있어야 할 듯
            if (contents != "") {
                System.out.println("수정된 내용: " + contents);
                found.update(contents);
            }
            System.out.println("입력이 없습니다.");

        } else {
            System.out.println("없는 메세지 잆니다.");
        }

    }

    // 메세지 삭제
    @Override
    public void deleteMessage(UUID mesUId) {
        Message meg = search(mesUId);

        if (meg != null) {
            System.out.println(meg.getMeg() + "메세지를 삭제했습니다.");
            Messages.remove(meg);
        } else {
            System.out.println("없거나 삭제된 메세지입니다.");
        }
    }

    // 메세지 찾아서 객체 넘기기
    @Override
    public Message search(UUID mesUId) {
//        이거는 여러명 검색을 위한 수정도 필요할 듯
//        아무래도 단체용은 따로 하는게 좋을것 같다.
        Message meg = Messages.stream().filter(u -> {
            return u.getId().equals(mesUId);
        }).collect(Collectors.toList()).get(0);


        return meg;
    }

    // 메세지 찾기
    @Override
    public void searchMessage(UUID mesUId) {
        Message meg = search(mesUId);

        if (meg != null) {
            System.out.println(meg.getMeg());
        } else {
            System.out.println("없는 존재입니다.");
        }
    }

    // 메세지'들' 찾기
    @Override
    public void searchMessageS(ArrayList<Message> megs) {
        // 메세지 내용을 검색하게 만들기
        megs.forEach(meg -> {
            Message sMeg = search(meg.getId());
            System.out.println(meg.getMeg());
        });
    }

    // 업데이트가 된적 있는 메세지 탐색
    @Override
    public void searchUpdateMessage() {
        AtomicBoolean notNull = new AtomicBoolean(false);

        Messages.forEach(u -> {
            if (u.getCreated() != u.getUpdated()) {
//                이거 uuid로 메세지 가져오고 내용 출력하게 만들어야 할 듯
                System.out.println("업데이트된 메세지: ");
                notNull.set(true);
            }
        });

        if (notNull.get() == false) {
            System.out.println("업데이트된 메세지가 없습니다.");
        }
    }
}