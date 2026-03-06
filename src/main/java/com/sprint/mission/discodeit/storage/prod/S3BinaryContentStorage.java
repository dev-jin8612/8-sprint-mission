package com.sprint.mission.discodeit.storage.prod;

import com.sprint.mission.discodeit.config.StorageProperties;
import com.sprint.mission.discodeit.dto.data.BinaryContentDTO;
import com.sprint.mission.discodeit.service.S3Service;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;
  private final long presignedUrlExpirationSeconds;
  private final String endpoint; // optional

  private final S3Service s3Service;
  private final S3Presigner presigner;

  public S3BinaryContentStorage(S3Service s3Service, StorageProperties.S3 props) {
    this.endpoint = props.endpoint();
    this.s3Service = s3Service;
    this.region = props.region();
    this.bucket = props.bucket();
    this.presigner = getPresigner();
    this.accessKey = props.accessKey();
    this.secretKey = props.secretKey();
    this.presignedUrlExpirationSeconds = props.presignedUrlExpiration();
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    String key = toKey(id);

    PutObjectRequest req = PutObjectRequest.builder()
        .bucket(s3Service.getBucketName())
        .key(key)
        .build();

    s3Service.getS3Client().putObject(req, RequestBody.fromBytes(bytes));
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    String key = toKey(id);

    GetObjectRequest req = GetObjectRequest.builder()
        .bucket(s3Service.getBucketName())
        .key(key)
        .build();

    return s3Service.getS3Client().getObject(req);
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDTO dto) {
    String key = toKey(dto.id());
    String url = generatePresignedUrl(key);

    return ResponseEntity
        .status(302)
        .header(HttpHeaders.LOCATION, url)
        .build();
  }

  private S3Client getS3Client() {
    AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);

    S3ClientBuilder builder = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(creds))
        .serviceConfiguration(S3Configuration.builder()
            .pathStyleAccessEnabled(true)
            .build());

    if (endpoint != null && !endpoint.isBlank()) {
      builder.endpointOverride(URI.create(endpoint.trim()));
    }

    return builder.build();
  }

  private S3Presigner getPresigner() {
    AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);

    S3Presigner.Builder builder = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(creds));

    if (endpoint != null && !endpoint.isBlank()) {
      builder.endpointOverride(URI.create(endpoint.trim()));
    }

    return builder.build();
  }

  private String generatePresignedUrl(String key) {
    GetObjectRequest getReq = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
        .getObjectRequest(getReq)
        .build();

    PresignedGetObjectRequest presigned = presigner.presignGetObject(presignReq);
    return presigned.url().toString();
  }

  private String toKey(UUID id) {
    // 키 설계는 프로젝트 정책에 맞추세요(예: prefix/디렉토리 분리)
    return "binary/" + id;
  }
}