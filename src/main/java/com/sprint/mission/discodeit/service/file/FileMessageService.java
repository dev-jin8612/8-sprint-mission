package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"),"data");
    private static final Path filepath = Paths.get(String.valueOf(directory), "meg.ser");
    private List<Message> messages;

    public FileMessageService() {
        init(directory);
        messages = load(filepath);
    }

    public static void init(Path directory) {
        // 저장할 경로의 파일 초기화
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> void save(Path directory, T data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directory.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Message> load(Path directory) {
        if (Files.exists(directory)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(directory.toFile()))) {
                Object data = ois.readObject();
                return (List<Message>) data;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    // 메세지 추가
    @Override
    public void addMessage(Message message) {
        messages.add(message);
        save(filepath, messages);
    }

    // 메세지 수정
    @Override
    public void updateMessage(UUID mesUid, String contents) {
        Message m = messages.stream().filter(ch1 -> ch1.getId().equals(mesUid)).findFirst().get();

        if (m != null) {
            if (contents != "") {
                System.out.println(m.getMeg() + " -> " + contents);
                m.update(contents);
                save(filepath, messages);
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
        Message m = messages.stream()
                .filter(u -> u.getId().equals(mesUId))
                .findFirst().get();

        if (m != null) {
            System.out.println(m.getMeg() + "를 삭제했습니다.");
            messages.remove(m);
            save(filepath, messages);
        } else {
            System.out.println("없거나 삭제된 메세지입니다.");
        }
    }

    // 메세지 찾아서 단일객체 넘기기
    public List<Message> search(String contents) {
        List<Message> meg = messages.stream()
                .filter(u -> u.getMeg().contains(contents))
                .collect(Collectors.toList());

        if (meg.isEmpty()) {
            return null;
        } else {
            return meg;
        }
    }

    @Override
    public void searchMessage(String contents) {
        List<Message> meg = search(contents);

        if (meg != null) {
            meg.stream().forEach(u -> System.out.println(u.getMeg() + "를 찾았습니다."));
        } else {
            System.out.println("삭제 됐거나 없는 메세지 입니다.");
        }
    }

    // 메세지'들' 찾기
    @Override
    public void searchMessageS(List<String> contents) {
        contents.forEach(content -> {
            searchMessage(content);
        });
    }

    // 업데이트가 된적 있는 메세지 탐색
    @Override
    public void searchUpdateMessage() {
        AtomicBoolean notNull = new AtomicBoolean(false);

        messages.forEach(u -> {
            if (u.getCreated() != u.getUpdated()) {
                // 이거 uuid로 메세지 가져오고 내용 출력하게 만들어야 할 듯
                System.out.println("업데이트된 메세지: " + u.getMeg());
                notNull.set(true);
            }
        });

        if (notNull.get() == false) {
            System.out.println("업데이트된 메세지가 없습니다.");
        }
    }
}
