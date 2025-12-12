package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class SaveLoadHelper<T> {

    // 저장할 경로의 파일 초기화
    public void init(Path directory) {
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

    public Map<UUID, T>  load(Path directory) {
        if (Files.exists(directory)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(directory.toFile()))) {
                Object data = ois.readObject();
                return (Map<UUID, T>) data;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new HashMap<>();
        }
    }
}
