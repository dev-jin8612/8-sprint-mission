package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserService implements UserService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data");
    private static final Path file = Paths.get(String.valueOf(directory), "user.ser");
    private final Map<UUID, User> users;

    public FileUserService() {
        init(directory);
        users = load(file);
    }

    public void init(Path directory) {
        // 저장할 경로의 파일 초기화
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T> void save(Path directory, T data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directory.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Map<UUID, User> load(Path directory) {
        if (Files.exists(directory)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(directory.toFile()))) {
                Object data = ois.readObject();
                return  (Map<UUID, User>) data;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new HashMap<UUID,User>();
        }
    }

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        save(file, users);
        return user;
    }

    // 사람 수정
    @Override
    public User update(UUID userid, String username) {
        User user = Optional.ofNullable(users.get(userid))
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        user.update(username);
            save(file, users);
        return user;
    }

    // 사람 삭제
    @Override
    public void delete(UUID userId) {
        if (!users.containsKey(userId)) {
            throw new NoSuchElementException("이미 삭제 되었습니다.");
        }

        users.remove(userId);
            save(file, users);
    }

    // 사람 찾아서 객체 넘기기
    @Override
    public List<User> searchByName(List<String> name) {
        List<User> result = users.values().stream()
                .filter(user ->
                        name.stream().anyMatch(na -> user.getUserName().contains(na))
                ).toList();

        return Optional.ofNullable(result)
                .orElse(null);
    }

    // 사람 찾기
    @Override
    public User findById(UUID id) {
        return Optional.ofNullable(users.get(id))
                .orElse(null);
    }

    // 유저 리스트 넘기는거 만들기
    // 채널 만들 때 필요
    @Override
    public List<User> getUsers() {
        List<User> user = new ArrayList<>(users.values());

        return Optional.ofNullable(user)
                .orElse(null);
    }
}
