# Phase 2: Spring Boot 애플리케이션 AWS 환경 대응

## 개요
Phase 1에서 완성한 로컬 환경 Spring Boot 메뉴 관리 시스템을 AWS 클라우드 환경에서 동작하도록 확장한다. RDS PostgreSQL 연결, S3 파일 업로드 서비스 구현, 환경변수 기반 설정 관리 등을 단계별로 진행한다.

---

## 1. AWS SDK 의존성 추가

### 1-1. build.gradle AWS SDK 추가

#### 1-1-1. Phase 1 완료 상태 확인
Phase 1에서 다음이 완료되어 있어야 한다:
- 로컬 PostgreSQL 데이터베이스 연결 확인
- 메뉴 CRUD 기능 정상 동작 확인
- 파일 업로드/다운로드 기능 정상 동작 확인

#### 1-1-2. AWS SDK 의존성 추가
`build.gradle` 파일의 `dependencies` 블록에 다음을 추가한다:

```gradle
dependencies {
    // 기존 Phase 1 의존성들...
    
    // AWS SDK 버전 관리를 위한 BOM (Bill of Materials)
    implementation platform('software.amazon.awssdk:bom:2.21.29')
    
    // Amazon S3 파일 업로드/다운로드/삭제를 위한 클라이언트
    implementation 'software.amazon.awssdk:s3'
    // Amazon RDS 데이터베이스 연결을 위한 클라이언트
    implementation 'software.amazon.awssdk:rds'
    // AWS 인증 및 권한 처리를 위한 라이브러리
    implementation 'software.amazon.awssdk:auth'
    
    // AWS SDK 핵심 공통 라이브러리
    implementation 'software.amazon.awssdk:aws-core'
    // AWS 리전(지역) 설정 및 관리 라이브러리
    implementation 'software.amazon.awssdk:regions'
}
```

**📚 AWS SDK 의존성 관련 참고 자료:**
- [AWS SDK for Java 2.x 공식 개발자 가이드](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
- [Gradle 프로젝트 설정 가이드](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/setup-project-gradle.html)
- [AWS SDK GitHub 레포지토리](https://github.com/aws/aws-sdk-java-v2)
- [Maven Central - AWS SDK 모듈 검색](https://search.maven.org/search?q=software.amazon.awssdk)
- [AWS SDK 버전 및 변경사항](https://github.com/aws/aws-sdk-java-v2/blob/master/CHANGELOG.md)

---

## 2. 환경변수 기반 설정 변경

### 2-1. application.yaml 환경변수 대응

#### 2-1-1. Phase 1 설정을 환경변수 기반으로 확장
Phase 1의 `application.yaml`을 기반으로 AWS 환경변수 지원을 추가한다:

```yaml
# Spring Boot 애플리케이션 설정
spring:
  profiles:
    active: local
  # 데이터베이스 연결 설정
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://${RDS_ENDPOINT:localhost:5432}/${RDS_DATABASE:ohgi_restaurant}
    username: ${RDS_USERNAME:ohgiraffers}
    password: ${RDS_PASSWORD:ohgiraffers}

  # JPA 설정
  jpa:
    hibernate:
      ddl-auto: update # 기존 데이터 보존하며 스키마 변경사항만 반영
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

  # SQL 초기화 설정
  sql:
    init:
      mode: always  # 애플리케이션 시작시 더미 데이터 자동 로드
      schema-locations: classpath:schema.sql
      data-locations: classpath:data.sql
      encoding: UTF-8

  # 파일 업로드 설정
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

# AWS 설정
aws:
  region: ap-northeast-2
  credentials:
    access-key: ${AWS_ACCESS_KEY_ID:}
    secret-key: ${AWS_SECRET_ACCESS_KEY:}
  s3:
    bucket: ${AWS_S3_BUCKET:menu-images-2025-ohgi}

# 서버 설정
server:
  port: 8080

# 로깅 설정
logging:
  level:
    com.ohgiraffers.awsdeploy: DEBUG
    org.springframework.web: DEBUG

# 파일 업로드 경로 설정 (build 폴더 직접 사용)
file:
  upload:
    path: build/resources/main/static/images/
```

#### 2-1-2. 환경별 프로파일 설정 생성

**1. application-local.yaml 생성:**
```yaml
# application-local.yaml
# 로컬 개발 환경 설정

# 로컬 환경용 데이터 로드 설정
spring:
  sql:
    init:
      data-locations: classpath:data-local.sql

file:
  upload-dir: uploads/
  base-url: /api/images/
```

**2. application-aws.yaml 생성:**
```yaml
# application-aws.yaml
# AWS 환경 설정 (환경변수를 통해 설정값 주입)

# AWS 환경용 데이터 로드 설정
spring:
  sql:
    init:
      data-locations: classpath:data.sql

file:
  upload-dir: s3
  base-url: https://${aws.s3.bucket}.s3.${aws.region}.amazonaws.com/
```

### 2-2. 환경변수 설정 방법

#### 2-2-1. IntelliJ IDEA 환경변수 설정
개발 중 빠른 테스트를 위한 방법이다:

**🔧 설정 방법:**
1. **Run** → **Edit Configurations** 클릭
2. **"Modify options"** 버튼(Alt+M) 클릭
3. **"Environment variables"** 옵션 체크
4. **Environment variables** 섹션에 다음 변수들 추가:
   ```
   AWS_ACCESS_KEY_ID=AKIA... (Phase 1에서 생성한 액세스 키 ID)
   AWS_SECRET_ACCESS_KEY=... (Phase 1에서 생성한 비밀 액세스 키)
   AWS_S3_BUCKET=menu-images-2025-ohgi (실제 버킷명)
   RDS_ENDPOINT=menu-management-db.xxxxxxxxx.ap-northeast-2.rds.amazonaws.com
   RDS_USERNAME=postgres
   RDS_PASSWORD=PostgresAdmin123!
   RDS_DATABASE=menudb
   ```
5. **Active profiles** 필드에 `aws` 입력

> **💡 중요**: `RDS_DATABASE=menudb`로 설정한다. 앞서 Phase1에서 생성한 데이터베이스 이름이다.

#### 2-2-2. .env.example 템플릿 파일 생성 (선택사항)
팀 공유 및 문서화를 위해 프로젝트 루트에 생성:

```env
# .env.example - 환경변수 템플릿 파일
# 실제 값은 IDE Environment variables에 설정

# AWS 자격 증명
AWS_ACCESS_KEY_ID=your_access_key_here
AWS_SECRET_ACCESS_KEY=your_secret_key_here
AWS_S3_BUCKET=menu-images-2025-ohgi

# RDS 데이터베이스 연결 정보
RDS_ENDPOINT=menu-management-db.xxxxxxxxx.ap-northeast-2.rds.amazonaws.com
RDS_USERNAME=postgres
RDS_PASSWORD=PostgresAdmin123!
RDS_DATABASE=menudb
```

---

## 3. RDS PostgreSQL 연결 테스트

### 3-1. AWS 프로파일로 애플리케이션 실행

#### 3-1-1. 실행 및 연결 확인
1. IntelliJ에서 환경변수 설정 완료 후 애플리케이션 실행
2. 콘솔에서 다음 로그 확인:
   ```
   The following 1 profile is active: aws
   Hikari - Starting...
   Hikari - Start completed.
   ```

#### 3-1-2. 연결 실패 시 체크리스트
- [ ] RDS 엔드포인트가 정확한지 확인
- [ ] 보안 그룹에서 현재 IP의 5432 포트 접근 허용 확인
- [ ] RDS 인스턴스가 `사용 가능` 상태인지 확인
- [ ] 환경변수가 올바르게 설정되었는지 확인

---

## 4. S3 파일 업로드 서비스 구현

### 4-1. S3Service 클래스 생성

#### 4-1-1. S3Service.java 파일 생성
`src/main/java/com/ohgiraffers/awsdeploy/service/S3Service.java`:

```java
package com.ohgiraffers.awsdeploy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;
    private final String region;

    /* S3Service 생성자 - 환경변수에서 AWS 설정을 주입받는다
     * 1. AWS 액세스 키
     * 2. AWS 시크릿 키
     * 3. S3 버킷 이름
     * 4. S3 리전
     */
    public S3Service(@Value("${aws.credentials.access-key}") String accessKey,
                     @Value("${aws.credentials.secret-key}") String secretKey,
                     @Value("${aws.s3.bucket}") String bucketName,
                     @Value("${aws.region}") String region) {
        
        this.bucketName = bucketName;
        this.region = region;
        
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    /**
     * S3에 파일을 업로드한다
     */
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    /**
     * S3에서 파일을 삭제한다
     */
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

    /**
     * 업로드된 파일의 전체 URL을 반환한다
     */
    public String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    /**
     * 고유한 파일명을 생성한다
     */
    private String generateFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
```

### 4-2. MenuService에 S3 연동 추가

#### 4-2-1. MenuService 수정
기존 MenuService에 S3Service 의존성을 추가하고 환경별 분기 로직을 구현한다:

```java
@Service
@Transactional(readOnly = true)
public class MenuService {
    
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadUtils fileUploadUtils;
    private final S3Service s3Service;  // S3Service 추가
    
    @Value("${spring.profiles.active}")
    private String activeProfile;  // 현재 활성 프로파일 확인
    
    @Autowired
    public MenuService(MenuRepository menuRepository, 
                       CategoryRepository categoryRepository,
                       FileUploadUtils fileUploadUtils,
                       S3Service s3Service) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
        this.fileUploadUtils = fileUploadUtils;
        this.s3Service = s3Service;
    }

    @Transactional
    public MenuDTO registerMenu(String menuName, Integer menuPrice, String menuDescription,
                               Long categoryCode, Integer menuStock, MultipartFile imageFile) {
        try {
            // 카테고리 조회
            Category category = categoryRepository.findById(categoryCode)
                    .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다: " + categoryCode));
            
            // 환경별 이미지 파일 처리
            String imageUrl = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                if ("aws".equals(activeProfile)) {
                    // AWS 환경: S3에 업로드
                    String fileName = s3Service.uploadFile(imageFile);
                    imageUrl = s3Service.getFileUrl(fileName);
                } else {
                    // 로컬 환경: 기존 로직 사용
                    if (!fileUploadUtils.isImageFile(imageFile)) {
                        throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
                    }
                    imageUrl = fileUploadUtils.uploadFile(imageFile);
                }
            }
            
            // 메뉴 엔티티 생성 및 저장
            Menu menu = new Menu(menuName, menuPrice, menuDescription, category, imageUrl, menuStock);
            Menu savedMenu = menuRepository.save(menu);
            
            return converttoDTO(savedMenu);
            
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public void deleteMenu(Long menuCode) {
        Menu menu = menuRepository.findById(menuCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다: " + menuCode));
        
        // 환경별 이미지 파일 삭제
        if (menu.getMenuImageUrl() != null && !menu.getMenuImageUrl().isEmpty()) {
            if ("aws".equals(activeProfile)) {
                // AWS 환경: S3에서 삭제
                String fileName = extractFileNameFromS3Url(menu.getMenuImageUrl());
                s3Service.deleteFile(fileName);
            } else {
                // 로컬 환경: 기존 로직 사용
                fileUploadUtils.deleteFile(menu.getMenuImageUrl());
            }
        }
        
        menuRepository.delete(menu);
    }

    private String extractFileNameFromS3Url(String s3Url) {
        return s3Url.substring(s3Url.lastIndexOf("/") + 1);
    }

    // 기타 메서드들...
}
```

---

## 5. 환경별 데이터 파일 분리

### 5-1. 데이터 파일 생성

#### 5-1-1. data-local.sql 생성 (로컬용)
로컬 환경에서 사용할 상대 경로 이미지:

```sql
-- 로컬 환경용 데이터 (상대 경로)
INSERT INTO tbl_menu (menu_name, menu_price, menu_description, menu_orderable, category_code, menu_image_url, menu_stock) 
VALUES 
('비빔밥', 12000, '건강한 비빔밥', 'Y', 4, 'images/bibimbap.jpg', 20),
('불고기', 18000, '부드러운 불고기', 'Y', 1, 'images/bulgogi.jpg', 15);
```

#### 5-1-2. data.sql 수정 (AWS용)
AWS 환경에서 사용할 S3 완전 URL:

```sql
-- AWS 환경용 데이터 (S3 완전 URL)
INSERT INTO tbl_menu (menu_name, menu_price, menu_description, menu_orderable, category_code, menu_image_url, menu_stock) 
VALUES 
('비빔밥', 12000, '건강한 비빔밥', 'Y', 4, 'https://menu-images-2025-ohgi.s3.ap-northeast-2.amazonaws.com/bibimbap.jpg', 20),
('불고기', 18000, '부드러운 불고기', 'Y', 1, 'https://menu-images-2025-ohgi.s3.ap-northeast-2.amazonaws.com/bulgogi.jpg', 15);
```

---

## 6. 프론트엔드 S3 이미지 URL 처리

### 6-1. JavaScript 수정

#### 6-1-1. 이미지 URL 타입 구분 로직 추가
프론트엔드에서 S3 URL과 로컬 URL을 구분하여 처리:

```javascript
// 메뉴 카드 생성 함수에서 이미지 URL 처리
function createMenuCard(menu) {
    const imageSrc = menu.menuImageUrl && menu.menuImageUrl.startsWith('https://') 
        ? menu.menuImageUrl 
        : `/api/images/${menu.menuImageUrl}`;
        
    return `
        <div class="menu-card" data-menu-code="${menu.menuCode}">
            <img src="${imageSrc}" alt="${menu.menuName}" class="menu-image">
            <!-- 나머지 코드... -->
        </div>
    `;
}

// 메뉴 상세 모달에서도 동일하게 적용
function openMenuDetailModal(menu) {
    const imageSrc = menu.menuImageUrl && menu.menuImageUrl.startsWith('https://') 
        ? menu.menuImageUrl 
        : `/api/images/${menu.menuImageUrl}`;
        
    document.getElementById('detailMenuImage').src = imageSrc;
    // 나머지 코드...
}
```

---

## 7. 통합 테스트

### 7-1. 로컬 환경 테스트
1. Active profiles를 `local`로 설정하고 실행
2. 메뉴 등록/삭제/조회 기능 확인
3. 로컬 파일 시스템에 이미지 저장 확인

### 7-2. AWS 환경 테스트
1. Active profiles를 `aws`로 설정하고 환경변수 설정 후 실행
2. RDS 연결 확인
3. 메뉴 등록 시 S3 업로드 확인
4. 메뉴 삭제 시 S3 삭제 확인
5. 웹 페이지에서 S3 이미지 정상 표시 확인

---

## 체크포인트

Phase 2 완료 후 확인사항:
- [ ] **AWS SDK 설정**
  - [ ] build.gradle에 AWS SDK 의존성 추가 완료
  - [ ] 프로젝트 빌드 성공 확인
- [ ] **환경변수 설정**
  - [ ] application.yaml 환경변수 기반으로 변경 완료
  - [ ] application-local.yaml, application-aws.yaml 생성 완료
  - [ ] IntelliJ 환경변수 설정 완료
- [ ] **데이터베이스 연동**
  - [ ] AWS 프로파일로 RDS 연결 성공
  - [ ] 기존 메뉴 데이터 정상 조회 확인
- [ ] **S3 파일 업로드**
  - [ ] S3Service 클래스 구현 완료
  - [ ] MenuService에 환경별 분기 로직 추가 완료
- [ ] **환경별 데이터 분리**
  - [ ] data-local.sql, data.sql 분리 완료
  - [ ] 각 환경에서 적절한 이미지 URL 표시 확인
- [ ] **통합 테스트**
  - [ ] 로컬 환경에서 모든 기능 정상 동작
  - [ ] AWS 환경에서 모든 기능 정상 동작
  - [ ] 환경 전환 시 문제없이 동작

**중요 정보:**
- AWS 프로파일 실행: `--spring.profiles.active=aws`
- RDS_DATABASE 설정: `menudb` (Phase 1에서 생성한 데이터베이스 이름)
- S3 버킷 URL: `https://[버킷명].s3.ap-northeast-2.amazonaws.com/`

**다음 단계**: Phase 3 - Docker 컨테이너화 및 ECS 배포
