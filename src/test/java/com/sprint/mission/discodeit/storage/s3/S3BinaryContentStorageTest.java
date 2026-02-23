package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.StorageConfig;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3Client;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Testcontainers
@SpringBootTest(classes = {StorageConfig.class})
class S3BinaryContentStorageTest {

  @Container
  static LocalStackContainer localstack = new LocalStackContainer("localstack/localstack:3.0.2")
      .withServices(S3);

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    r.add("discodeit.storage.type", () -> "s3");
    r.add("discodeit.storage.s3.access-key", () -> localstack.getAccessKey());
    r.add("discodeit.storage.s3.secret-key", () -> localstack.getSecretKey());
    r.add("discodeit.storage.s3.region", () -> localstack.getRegion());
    r.add("discodeit.storage.s3.bucket", () -> "test-bucket");
    r.add("discodeit.storage.s3.presigned-url-expiration", () -> "600");
    r.add("discodeit.storage.s3.endpoint", () -> localstack.getEndpointOverride(S3).toString());
  }

  @Autowired
  BinaryContentStorage storage;

  static S3Client adminS3;

  @BeforeAll
  static void setUpBucket() {
    adminS3 = S3Client.builder()
        .endpointOverride(localstack.getEndpointOverride(S3))
        .region(software.amazon.awssdk.regions.Region.of(localstack.getRegion()))
        .credentialsProvider(
            software.amazon.awssdk.auth.credentials.StaticCredentialsProvider.create(
                software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create(
                    localstack.getAccessKey(), localstack.getSecretKey()
                )
            )
        )
        .serviceConfiguration(
            software.amazon.awssdk.services.s3.S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build()
        )
        .build();

    adminS3.createBucket(b -> b.bucket("test-bucket"));
  }

  @Test
  void put_and_get_success() throws Exception {
    UUID id = UUID.randomUUID();
    byte[] data = "hello".getBytes(StandardCharsets.UTF_8);

    storage.put(id, data);

    try (InputStream in = storage.get(id)) {
      byte[] read = in.readAllBytes();
      assertThat(new String(read, StandardCharsets.UTF_8)).isEqualTo("hello");
    }
  }

  @Test
  void download_shouldRedirectToPresignedUrl() {
    UUID id = UUID.randomUUID();
    storage.put(id, "x".getBytes(StandardCharsets.UTF_8));

    // 프로젝트 BinaryContentDto에 맞게 생성자/팩토리만 맞추시면 됩니다.
    BinaryContentDto dto = new BinaryContentDto(
        id,
        "x.txt",
        1L,
        "text/plain"
    );

    ResponseEntity<?> res = storage.download(dto);

    assertThat(res.getStatusCode().value()).isEqualTo(302);
    assertThat(res.getHeaders().getLocation()).isNotNull();
    assertThat(res.getHeaders().getLocation().toString()).contains("X-Amz-Signature");
  }
}