package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class FileUserService implements UserService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path file = Paths.get(String.valueOf(directory), "user.ser");
    private List<User> users;

    public FileUserService() {
        init(directory);
        users = load(file);
//        users.forEach(u -> {
//            System.out.println(u.getUserName());
//        });
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

    public static List<User> load(Path directory) {
        if (Files.exists(directory)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(directory.toFile()))) {
                Object data = ois.readObject();
                return (List<User>) data;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    public void addUser(User user) {
        users.add(user);
        save(file, users);
    }

    // 사람 수정
    @Override
    public void updateUser(UUID userid, String username) {
        User user = users.stream().filter(c -> c.getId().equals(userid)).findFirst().get();

        if (user != null) {
            System.out.println(user.getUserName() + " -> " + username);
            user.update(username);
            save(file, users);
        } else if (username != null) {
            System.out.println("입력이 잘못 되었습니다.");
        }
    }

    // 사람 삭제
    @Override
    public void deleteUser(UUID userId) {
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst().get();

        if (user != null) {
            System.out.println(user.getUserName() + "님을 삭제했습니다.");
            users.remove(user);
            save(file, users);
        } else {
            System.out.println("없거나 삭제된 존재입니다.");
        }

    }

    // 사람 찾아서 객체 넘기기
    @Override
    public List<User> search(String name) {
        List<User> user = users.stream()
                .filter(u -> u.getUserName().contains(name))
                .collect(Collectors.toList());

        if (user.isEmpty()) {
            return null;
        } else {
            return user;
        }
    }

    // 사람 찾기
    @Override
    public void searchUser(String name) {
        List<User> user = search(name);

        if (user != null) {
            user.stream().forEach(u -> System.out.println(u.getUserName() + "님이 존재합니다."));
        } else {
            System.out.println("없는 존재입니다.");
        }
    }

    // 사람'들' 찾기
    @Override
    public void searchUserS(List<String> names) {
        names.forEach(name -> searchUser(name));
    }

    // 업데이트가 된적 있는 사람 탐색
    @Override
    public void searchUpdateUser() {
        AtomicBoolean notNull = new AtomicBoolean(false);

        users.forEach(u -> {
            if (u.getCreated() != u.getUpdated()) {
                System.out.println("수정된적 있는 이름: " + u.getUserName());
                notNull.set(true);
            }
        });

        if (notNull.get() == false) {
            System.out.println("업데이트된 존재가 없습니다.");
        }
    }

    // 유저 리스트 넘기는거 만들기
    // 채널 만들 때 필요
    public List<User> getUsers() {
        return this.users;
    }

}
