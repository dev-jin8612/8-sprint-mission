package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.*;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AWSS3Test {

  private static Properties env;
  private static S3Client s3;
  private static S3Presigner presigner;

  private static String bucket;
  private static String region;

  // 테스트에서 사용할 객체 키(업로드/다운로드/프리사인드 공통)
  private static String testKey;

  @BeforeAll
  static void setUp() throws Exception {
    env = loadDotEnv(Paths.get(".env"));

    String accessKey = required(env, "AWS_ACCESS_KEY_ID");
    String secretKey = required(env, "AWS_SECRET_ACCESS_KEY");
    region = required(env, "AWS_REGION");
    bucket = required(env, "AWS_S3_BUCKET");

    AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);

    S3ClientBuilder s3Builder = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(creds))
        // 경로 스타일이 필요한 S3 호환 스토리지(로컬스택 등)면 true가 도움이 됩니다.
        .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build());

    S3Presigner.Builder presignerBuilder = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(creds));

    // (선택) 엔드포인트 지정 (로컬스택 등)
    String endpoint = env.getProperty("AWS_S3_ENDPOINT");
    if (endpoint != null && !endpoint.isBlank()) {
      URI uri = URI.create(endpoint.trim());
      s3Builder.endpointOverride(uri);
      presignerBuilder.endpointOverride(uri);
    }

    s3 = s3Builder.build();
    presigner = presignerBuilder.build();

    testKey = "aws-s3-test/" + UUID.randomUUID() + ".txt";
  }

  @AfterAll
  static void tearDown() {
    // 테스트용 오브젝트 정리(실패해도 최대한 지우기)
    if (s3 != null) {
      try {
        s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(testKey).build());
      } catch (Exception ignored) {
      }
      s3.close();
    }
    if (presigner != null) presigner.close();
  }

  @Test
  @DisplayName("업로드 테스트")
  void upload() {
    String content = "hello s3 - " + UUID.randomUUID();
    PutObjectRequest req = PutObjectRequest.builder()
        .bucket(bucket)
        .key(testKey)
        .contentType("text/plain")
        .build();

    PutObjectResponse res = s3.putObject(req, RequestBody.fromString(content, StandardCharsets.UTF_8));
    assertNotNull(res.eTag());

    // 업로드 확인용 HEAD
    HeadObjectResponse head = s3.headObject(HeadObjectRequest.builder().bucket(bucket).key(testKey).build());
    assertTrue(head.contentLength() > 0);
  }

  @Test
  @DisplayName("다운로드 테스트")
  void download() {
    // 먼저 업로드(다운로드 단독 실행 대비)
    String content = "download-test - " + UUID.randomUUID();
    s3.putObject(
        PutObjectRequest.builder().bucket(bucket).key(testKey).contentType("text/plain").build(),
        RequestBody.fromString(content, StandardCharsets.UTF_8)
    );

    ResponseBytes<GetObjectResponse> bytes = s3.getObjectAsBytes(
        GetObjectRequest.builder().bucket(bucket).key(testKey).build()
    );

    String downloaded = bytes.asString(StandardCharsets.UTF_8);
    assertEquals(content, downloaded);
  }

  @Test
  @DisplayName("PresignedUrl 생성 테스트 (GET)")
  void createPresignedUrl() {
    // 먼저 업로드(프리사인드 URL로 실제 접근 테스트 대비)
    s3.putObject(
        PutObjectRequest.builder().bucket(bucket).key(testKey).contentType("text/plain").build(),
        RequestBody.fromString("presign-test", StandardCharsets.UTF_8)
    );

    GetObjectRequest getReq = GetObjectRequest.builder()
        .bucket(bucket)
        .key(testKey)
        .build();

    GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .getObjectRequest(getReq)
        .build();

    PresignedGetObjectRequest presigned = presigner.presignGetObject(presignReq);
    String url = presigned.url().toString();

    assertNotNull(url);
    assertTrue(url.startsWith("http"));
    System.out.println("Presigned GET URL: " + url);

    // 여기서는 "생성"만 검증(실제 호출까지 하려면 HttpClient로 GET 요청 추가)
  }

  private static Properties loadDotEnv(Path envPath) throws IOException {
    if (!Files.exists(envPath)) {
      throw new FileNotFoundException(".env 파일을 찾을 수 없습니다: " + envPath.toAbsolutePath());
    }

    Properties p = new Properties();
    for (String line : Files.readAllLines(envPath, StandardCharsets.UTF_8)) {
      String trimmed = line.trim();
      if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

      int idx = trimmed.indexOf('=');
      if (idx < 0) continue;

      String key = trimmed.substring(0, idx).trim();
      String val = trimmed.substring(idx + 1).trim();

      // "value" 형태면 따옴표 제거
      if (val.length() >= 2 && val.startsWith("\"") && val.endsWith("\"")) {
        val = val.substring(1, val.length() - 1);
      }
      p.setProperty(key, val);
    }
    return p;
  }

  private static String required(Properties p, String key) {
    String v = p.getProperty(key);
    if (v == null || v.isBlank()) {
      throw new IllegalStateException(".env에 필수 값이 없습니다: " + key);
    }
    return v.trim();
  }
}