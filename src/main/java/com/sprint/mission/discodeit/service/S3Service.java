package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.config.StorageProperties; // 설정 클래스 임포트
import java.io.IOException;
import java.util.UUID;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Getter
public class S3Service {

  private final S3Client s3Client;
  private final String bucketName;
  private final String region;

  public S3Service(StorageProperties props) {
    StorageProperties.S3 s3Props = props.s3();
    this.bucketName = s3Props.bucket();
    this.region = s3Props.region();

    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        s3Props.accessKey(),
        s3Props.secretKey()
    );

    this.s3Client = S3Client.builder()
        .region(Region.of(this.region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  /** S3에 파일을 업로드한다 */
  public String uploadFile(MultipartFile file) {
    try {
      String fileName = generateFileName(file.getOriginalFilename());

      PutObjectRequest putRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(fileName)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(putRequest,
          RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      return fileName;
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload file to S3", e);
    }
  }

  /** S3에서 파일을 삭제한다 */
  public void deleteFile(String fileName) {
    try {
      DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(fileName)
          .build();

      s3Client.deleteObject(deleteRequest);
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete file from S3", e);
    }
  }

  /** 업로드된 파일의 전체 URL을 반환한다 */
  public String getFileUrl(String fileName) {
    return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
  }

  /** 고유 파일명 생성한다 */
  private String generateFileName(String originalFilename) {
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    return UUID.randomUUID().toString() + extension;
  }
}