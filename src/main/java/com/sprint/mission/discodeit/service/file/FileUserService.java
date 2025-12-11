package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public User create(User user) {
        users.add(user);
        save(file, users);
        return user;
    }

    // 사람 수정
    @Override
    public User update(UUID userid, String username) {
        User user = users.stream().filter(c -> c.getId().equals(userid)).findFirst().get();

        if (user != null) {
            user.update(username);
            save(file, users);
            return user;
        } else return null;
    }

    // 사람 삭제
    @Override
    public void delete(UUID userId) {
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst().get();

        if (user != null) {
            users.remove(user);
            save(file, users);
        }
    }

    // 사람 찾아서 객체 넘기기
    @Override
    public List<User> searchByName(List<String> name) {
        List<User> result = users.stream()
                .filter(user ->
                        name.stream().anyMatch(na -> user.getUserName().contains(na))
                ).toList();

        return result.isEmpty() ? null : result;
    }

    // 사람 찾기
    @Override
    public User findById(UUID id) {
        Optional<User> user = users.stream()
                .filter(c -> c.getId().equals(id)).findFirst();

        return user.orElse(null);
    }

    // 유저 리스트 넘기는거 만들기
    // 채널 만들 때 필요
    @Override
    public List<User> getUsers() {
        return this.users;
    }

}
