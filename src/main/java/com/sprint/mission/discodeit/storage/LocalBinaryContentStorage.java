package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Conditional(LocalStorageCondition.class)
@RequiredArgsConstructor
public class LocalBinaryContentStorage implements BinaryContentStorage {

  @Value("${discodeit.storage.local.root-path}")
  private String rootPath;

  private Path root;

  @PostConstruct
  public void init() throws IOException {
    this.root = Paths.get(rootPath);
    Files.createDirectories(root);
  }

  private Path resolvePath(UUID binaryContentId) {
    return root.resolve(binaryContentId.toString());
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    try {
      Files.write(resolvePath(binaryContentId), bytes);
      return binaryContentId;
    } catch (IOException e) {
      throw new IllegalStateException("파일 저장 실패", e);
    }
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    try {
      return Files.newInputStream(resolvePath(binaryContentId));
    } catch (IOException e) {
      throw new IllegalStateException("파일 조회 실패", e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDTO dto) {
    InputStream inputStream = get(dto.id());

    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + dto.fileName() + "\""
        )
        .header(HttpHeaders.CONTENT_TYPE, dto.contentType())
        .contentLength(dto.size())
        .body(resource);
  }
}
