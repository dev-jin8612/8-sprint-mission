# Phase 1: AWS 기본 설정

## 개요
Spring Boot 메뉴 관리 시스템을 AWS 클라우드 환경으로 마이그레이션하기 위한 기본 인프라 설정을 진행한다. IAM 사용자 생성, S3 버킷 설정, RDS PostgreSQL 인스턴스 생성 순으로 진행한다.

---

## 1. AWS 계정 및 IAM 설정

### 1-1. Root 계정 보안 강화

#### 1-1-1. MFA(Multi-Factor Authentication) 설정
1. **AWS Management Console**에 Root 사용자로 로그인
2. 우측 상단 계정명 클릭 → **보안 자격 증명** 선택
3. **멀티 팩터 인증(MFA)** 섹션에서 **MFA 디바이스 할당** 클릭
4. MFA 디바이스 유형 선택:
   - **인증 앱** 권장 (Google Authenticator, Microsoft Authenticator 등)
5. 디바이스명 입력 후 **다음** 클릭
6. 인증 앱으로 QR 코드 스캔
7. 연속된 두 개의 MFA 코드 입력 후 **MFA 추가** 클릭

#### 1-1-2. 결제 알림 설정
1. 우측 상단 계정명 클릭 → **결제 및 비용 관리** 이동
2. 좌측 네비게이션 하단의 **빌링 기본 설정** 이동
3. 청구서 기본 설정 페이지 도착 후 **알림 기본 설정**의 편집 버튼 클릭
   1. AWS 프리 티어 알림 수신 → 체크
   2. CloudWatch 결제 알림 수신 → 체크 (체크 후 해제 불가능하므로 알아두자)
   3. **업데이트** 클릭
4. **CloudWatch** → **경보 상태** → **결제** → **경보 생성**에서 월 사용량 알림 설정
5. 버지니아 북부로 바꾸고 지표선택 → Billing → 예상요금 합계 → USD 체크 → 지표선택
6. 그리고 패널에서 원하는 설정으로 바꾸기(상단은 변경이 거의 불가)
7. 나머지도 원하는데로 수정하기

### 1-2. IAM 사용자 생성 및 설정

#### 1-2-1. 개발용 IAM 사용자 생성
1. **IAM 콘솔** → **사용자** → **사용자 생성** 클릭
2. 사용자 정보 입력:
   - **사용자 이름**: `developer-owl`과 같이 원하는 이름 입력
   - **AWS Management Console에 대한 사용자 액세스 권한 제공** 체크
   - **IAM 사용자를 생성하고 싶음** 선택 - 없어진 듯
   - **사용자 지정 암호** 선택 후 강력한 비밀번호 입력
   - **사용자는 다음 로그인 시 새 암호를 생성해야 합니다** 해제 권장
3. **다음** 클릭

#### 1-2-2. 권한 정책 연결
**권한 옵션**에서 **직접 정책 연결** 선택 후 다음 정책들을 검색하여 선택:

- `AmazonS3FullAccess`
  - S3 버킷 생성, 객체 업로드/다운로드/삭제 권한
- `AmazonRDSFullAccess`
  - RDS 인스턴스 생성, 관리, 접근 권한
- `AmazonEC2FullAccess` - 일반 사원은 원래 안주는 권한
  - VPC, 보안 그룹, 네트워크 관리 권한
- `IAMReadOnlyAccess`
  - IAM 리소스 조회 권한

**다음** → 내용 검토 후 **사용자 생성** 클릭

#### 1-2-3. IAM 사용자 MFA 설정
1. 생성된 사용자 클릭 → **보안 자격 증명** 탭
2. **멀티 팩터 인증(MFA)** 섹션에서 **MFA 디바이스 할당** 클릭
3. Root 계정과 동일한 방식으로 MFA 설정 진행

#### 1-2-4. 액세스 키 생성
1. **보안 자격 증명** 탭에서 **액세스 키** 섹션
2. **액세스 키 만들기** 클릭
3. **사용 사례** 선택: **로컬 코드** 선택
4. **위의 권장 사항을 이해했으며 액세스 키 생성을 계속 진행하겠습니다** 체크 후 **다음**
5. **설명 태그** 입력: `SpringBoot Application Deploy Practice` 등
6. **액세스 키 만들기** 클릭
7. **액세스 키 ID**와 **비밀 액세스 키**를 안전한 곳에 저장
   - 이 정보는 Spring Boot 애플리케이션에서 AWS 서비스 연동 시 사용됨
   - **비밀 액세스 키는 이 화면에서만 확인 가능**하므로 반드시 저장
   - CSV 파일로 다운받아 놓고 본인의 Google Drive나 OneDrive, iCloud 등 클라우드 보관소에 저장해두고 사용하는 것을 권장

#### 1-2-5. IAM 사용자로 재로그인 확인
1. AWS Console에서 로그아웃
2. **IAM 사용자 로그인 URL** 사용: `https://[계정ID].signin.aws.amazon.com/console`
3. 생성한 IAM 사용자 계정으로 로그인 확인

---

## 2. S3 버킷 생성 및 설정

### 2-1. 이미지 저장용 S3 버킷 생성

#### 2-1-1. S3 버킷 생성
1. **S3 콘솔** → **버킷** → **버킷 만들기** 클릭
2. 버킷 설정:
   - **버킷 이름**: `menu-images-[고유식별자]` (예: `menu-images-2024-ohgi`)
     - 버킷명은 전 세계적으로 고유해야 하므로 날짜나 팀명 등을 포함
   - **AWS 리전**: `아시아 태평양(서울) ap-northeast-2` 선택
3. **객체 소유권** 설정:
   - **ACL 비활성화됨(권장)** 선택 (기본값)
4. **퍼블릭 액세스 차단 설정** 설정:
   - **모든 퍼블릭 액세스 차단** 해제 (체크 해제)
   - 경고 메시지 확인 후 **현재 설정으로 인해 이 버킷과 그 안의 객체가 퍼블릭 상태가 될 수 있음을 알고 있습니다** 체크
5. **버킷 버전 관리**: **비활성화** (기본값)
6. **기본 암호화**: **Amazon S3 관리형 키(SSE-S3)를 사용한 서버 측 암호화** (기본값)
7. **버킷 만들기** 클릭

#### 2-1-2. 버킷 정책 설정 (퍼블릭 읽기 허용)
1. 생성된 버킷 클릭 → **권한** 탭
2. **버킷 정책** 섹션에서 **편집** 클릭
3. 다음 정책 입력 (버킷명을 실제 생성한 버킷명으로 변경):

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PublicReadGetObject",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::menu-images-[고유식별자]/*"
        }
    ]
}
```

4. **변경 사항 저장** 클릭

> **📌 참고: Access Analyzer 권한 경고**
> 
> 정책 편집 시 하단에 "권한이 필요함" 경고가 표시될 수 있다:
> ```
> User: arn:aws:iam::xxxxxx:user/developer-owl is not authorized to perform: access-analyzer:ValidatePolicy
> ```
> 
> - **의미**: AWS가 정책 보안성을 자동 검증하려 하지만 해당 권한이 없다는 의미
> - **영향**: 버킷 정책 적용에는 전혀 영향 없음 (무시해도 됨)
> - **해결 (선택)**: 이 경고를 보고 싶지 않다면 IAM 사용자에 `AccessAnalyzerFullAccess` 정책을 추가하면 된다

### 2-2. S3 CORS 설정

#### 2-2-1. CORS 규칙 설정
1. **권한** 탭에서 **교차 오리진 리소스 공유(CORS)** 섹션
2. **편집** 클릭 후 다음 CORS 설정 입력:

```json
[
    {
        "AllowedHeaders": [
            "*"
        ],
        "AllowedMethods": [
            "GET",
            "PUT",
            "POST",
            "DELETE",
            "HEAD"
        ],
        "AllowedOrigins": [
            "*"
        ],
        "ExposeHeaders": [
            "ETag"
        ],
        "MaxAgeSeconds": 3000
    }
]
```

3. **변경 사항 저장** 클릭

#### 2-2-2. 기존 메뉴 이미지 업로드
1. **객체** 탭에서 **업로드** 클릭
2. **파일 추가** 클릭하여 로컬 프로젝트의 `src/main/resources/static/images/` 폴더의 모든 이미지 파일 선택
3. **대상** 설정에서 **이 버킷의 대상 지정** 선택
4. **대상**: `images/` 입력 (폴더 구조 유지)
5. **업로드** 클릭

#### 2-2-3. S3 URL 확인 및 테스트
1. 업로드된 이미지 중 하나 클릭
2. **객체 URL** 복사 (예: `https://menu-images-2024-ohgi.s3.ap-northeast-2.amazonaws.com/images/americano.jpg`)
3. 브라우저에서 URL 접속하여 이미지가 정상적으로 표시되는지 확인

---

## 체크포인트

목차 2-1, 2-2 완료 후 확인사항:
- [ ] S3 버킷 생성 완료 (고유한 버킷명 사용)
- [ ] 퍼블릭 액세스 설정 완료
- [ ] 버킷 정책 설정 완료 (퍼블릭 읽기 허용)
- [ ] CORS 설정 완료
- [ ] 기존 메뉴 이미지 S3 업로드 완료
- [ ] S3 이미지 URL 브라우저 접속 확인 완료

**다음 단계**: 3. RDS PostgreSQL 인스턴스 생성

---

## 3. RDS PostgreSQL 인스턴스 생성

### 3-1. VPC 보안 그룹 생성

#### 3-1-1. 보안 그룹 생성
1. **EC2 콘솔** → **네트워크 및 보안** → **보안 그룹** → **보안 그룹 생성** 클릭
2. 보안 그룹 정보 입력:
   - **보안 그룹 이름**: `rds-postgresql-sg`
   - **설명**: `Security Group for PostgreSQL RDS instance`
   - **VPC**: 기본 VPC 선택
3. **인바운드 규칙** 설정:
   - **규칙 추가** 클릭
   - **유형**: `PostgreSQL` 선택 (자동으로 Port 5432 설정됨)
   - **프로토콜**: `TCP` (자동 설정)
   - **포트 범위**: `5432` (자동 설정)
   - **소스**:`내 IP` 선택 (개발 환경에서 내 IP 주소로부터만 접근 허용)
   - **설명**: `Only allow with my personal IP address`
   - 필요한 경우, **규칙 추가**를 다시 클릭하여 `Anywhere-IPv4 (0.0.0.0/0)` 추가하면 내 IP 주소 뿐만 아니라 어디서든 접근 가능하며 실제 운영 환경에서는 권장하지 않음
4. **보안 그룹 생성** 클릭

### 3-2. RDS PostgreSQL 인스턴스 생성

#### 3-2-1. 데이터베이스 인스턴스 생성
1. **RDS 콘솔** → **데이터베이스** → **데이터베이스 생성** 클릭
2. **엔진 옵션** 설정:
   - **엔진 유형**: `PostgreSQL` 선택
   - **버전**: `PostgreSQL 17.5-R1` (또는 최신 17.x 버전)
3. **템플릿** 설정:
   - **프리 티어** 선택 (학습 목적)

	> **📌 참고: 가용성 및 내구성 옵션**
	> 
	> 프리 티어 선택 시 단일 AZ DB 인스턴스(인스턴스 1개)가 자동 선택된다:
	> - **단일 AZ**: 하나의 가용 영역에만 데이터베이스 인스턴스가 생성됨
	> - **비용 효율적**: 프리 티어 혜택으로 무료 사용 가능
	> - **고가용성 없음**: 해당 AZ에 장애 발생 시 서비스 중단 가능
	> 
	> 운영 환경에서는 **다중 AZ DB 인스턴스** 또는 **다중 AZ DB 클러스터**를 고려해야 하지만, 학습 목적으로는 단일 AZ로 충분하다.

4. **설정** 설정:
   - **DB 인스턴스 식별자**: `menu-management-db`
   - **마스터 사용자 이름**: `postgres` (기본값)
   - **마스터 암호**: 강력한 비밀번호 입력 (예: `PostgresAdmin123!`)
   - **암호 확인**: 위와 동일한 비밀번호 재입력
5. **인스턴스 구성** 설정:
   - **DB 인스턴스 클래스**: `db.t3.micro` (프리티어)
6. **스토리지** 설정:
   - **스토리지 유형**: `범용 SSD(gp2)` (기본값)
   - **할당된 스토리지**: `20` GiB (기본값)
   - **추가 스토리지 구성**의 **스토리지 자동 조정**: 해제 권장 (비용 절약)

#### 3-2-2. 연결 설정
1. **연결** 설정:
   - **컴퓨팅 리소스**: `EC2 컴퓨팅 리소스에 연결 안 함` 선택
   - **VPC**: 기본 VPC 선택
   - **DB 서브넷 그룹**: `기본값` 선택
   - **퍼블릭 액세스**: `예` 선택 (개발 환경에서 외부 접근 허용)
   - **VPC 보안 그룹**: `기존 항목 선택` 선택
   - **기존 VPC 보안 그룹**: 앞서 생성한 `rds-postgresql-sg` 선택
   - **가용 영역**: `기본 설정 없음` (기본값)
   - **추가 구성**의 **데이터베이스 포트**: `5432` (기본값)

> **📌 참고: 모니터링 설정 시 나타나는 경고들**
> 
> RDS 생성 과정에서 다음과 같은 경고가 표시될 수 있으나 모두 무시해도 됨:
> - **KMS 액세스 거부**: 기본 암호화 사용으로 별도 KMS 권한 불필요
> - **KMS 키 변경 불가**: 생성 후 암호화 키 변경 제한 안내 (정상)

#### 3-2-3. 추가 설정
1. **데이터베이스 옵션** 설정:
   - **초기 데이터베이스 이름**: `menudb` 입력
   - **DB 파라미터 그룹**: `default.postgres17` (기본값)
   - **옵션 그룹**: `default:postgres-17` (기본값)
2. **백업** 설정:
   - **자동 백업 활성화** 체크 (기본값)
   - **백업 보존 기간**: `7일` (기본값)
3. **모니터링** 설정:
   - **향상된 모니터링 활성화** 해제 (비용 절약)
4. **데이터베이스 생성** 클릭

### 3-3. 데이터베이스 연결 확인

#### 3-3-1. 엔드포인트 정보 확인
1. RDS 인스턴스 생성 완료까지 대기 (약 5-10분 소요)
2. **데이터베이스** 목록에서 `menu-management-db` 클릭
3. **연결 및 보안** 탭에서 다음 정보 확인:
   - **엔드포인트**: `menu-management-db.xxxxxxxxx.ap-northeast-2.rds.amazonaws.com`
   - **포트**: `5432`

#### 3-3-2. DataGrip을 통한 연결 테스트
1. DataGrip 실행
2. **Database** 패널에서 **+** 클릭
3. **Data Source** → **PostgreSQL** 선택
4. **General** 탭에서 연결 정보 입력:
   - **Name**: `AWS RDS PostgreSQL` (연결 이름)
   - **Host**: RDS 엔드포인트 입력
   - **Port**: `5432`
   - **Database**: `menudb`
   - **User**: `postgres`
   - **Password**: 설정한 마스터 비밀번호 입력
5. **Test Connection** 클릭하여 연결 확인
6. 연결 성공 후 **OK** 클릭

#### 3-3-3. 스키마 및 데이터 초기화
연결 확인 후 다음 SQL 스크립트 실행:

**1. 스키마 생성 (schema.sql):**
```sql
-- 카테고리 테이블
DROP TABLE IF EXISTS tbl_category CASCADE;
CREATE TABLE tbl_category (
    category_code SERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
);

-- 메뉴 테이블
DROP TABLE IF EXISTS tbl_menu CASCADE;
CREATE TABLE tbl_menu (
    menu_code SERIAL PRIMARY KEY,
    menu_name VARCHAR(255) NOT NULL,
    menu_price INTEGER NOT NULL,
    menu_description TEXT,
    menu_orderable CHAR(1) NOT NULL DEFAULT 'Y',
    category_code INTEGER REFERENCES tbl_category(category_code),
    menu_image_url VARCHAR(255),
    menu_stock INTEGER NOT NULL DEFAULT 0
);
```

**2. 초기 데이터 삽입 (data.sql):**

**주의사항**: 이때, `image_url` 컬럼의 값들을 실제 S3 버킷 URL로 변경해야 한다.
예: `https://menu-images-2024-ohgi.s3.ap-northeast-2.amazonaws.com/images/americano.jpg`

```sql
-- 카테고리 데이터
INSERT INTO tbl_category (category_name) VALUES ('식사');
INSERT INTO tbl_category (category_name) VALUES ('디저트');
INSERT INTO tbl_category (category_name) VALUES ('음료');

-- 메뉴 데이터 (21개 전체)
INSERT INTO tbl_menu (menu_name, menu_price, menu_description, menu_orderable, category_code, menu_image_url, menu_stock) 
VALUES ('열무김치라떼', 4500, '열무로 만든 김치 라떼', 'Y', 3, 'https://menu-images-2025-ohgi.s3.ap-northeast-2.amazonaws.com/06a0060ae2da4dffb9a8a440ba5d9c5e.PNG', 10);

INSERT INTO tbl_menu (menu_name, menu_price, menu_description, menu_orderable, category_code, menu_image_url, menu_stock) 
VALUES ('우럭스무디', 5000, '우럭으로 만든 스무디', 'Y', 3, 'https://menu-images-2025-ohgi.s3.ap-northeast-2.amazonaws.com/fcb3e0c8f94940cf99724d26e6020259.PNG', 15);

INSERT INTO tbl_menu (menu_name, menu_price, menu_description, menu_orderable, category_code, menu_image_url, menu_stock) 
VALUES ('생갈치쉐이크', 6000, '생으로 갈아만든 갈치 쉐이크', 'Y', 3, 'https://menu-images-2025-ohgi.s3.ap-northeast-2.amazonaws.com/8e2492fd197e42d5855ffbbb5142b4ed.PNG', 17);
...
```

---

## 4. 과금 방지를 위한 리소스 정리 가이드

### ⚠️ 중요 안내
**실습 종료 후 반드시 아래 순서대로 리소스를 정리해야 한다.**
RDS 인스턴스는 중지해도 7일 후 자동 시작되며, S3 스토리지도 지속적으로 과금된다.

### 4-1. RDS 인스턴스 삭제 (최우선)

#### 4-1-1. RDS 인스턴스 삭제
1. **RDS 콘솔** → **데이터베이스** → `menu-management-db` 선택
2. **작업** → **삭제** 클릭
3. 삭제 옵션 설정:
   - **최종 스냅샷 생성**: `체크 해제` (학습용이므로 백업 불필요)
   - **자동화된 백업 보존**: `체크 해제`
   - **확인**: `delete me` 입력
4. **삭제** 클릭

> **📌 참고**: RDS 인스턴스는 삭제까지 약 5-10분 소요된다.

#### 4-1-2. DB 서브넷 그룹 삭제 (선택사항)
1. **RDS 콘솔** → **서브넷 그룹** → `default` 확인
2. 커스텀 서브넷 그룹을 생성했다면 해당 서브넷 그룹도 삭제

### 4-2. S3 버킷 및 객체 삭제

#### 4-2-1. S3 객체 전체 삭제
1. **S3 콘솔** → 생성한 버킷 (`menu-images-[고유식별자]`) 클릭
2. **객체** 탭에서 **전체 선택** 체크박스 클릭
3. **삭제** 클릭
4. **확인**: `영구적으로 삭제` 입력
5. **객체 삭제** 클릭

#### 4-2-2. S3 버킷 삭제
1. **S3 콘솔** → **버킷** 목록에서 해당 버킷 선택
2. **삭제** 클릭
3. **확인**: 버킷명 입력 (예: `menu-images-2024-ohgi`)
4. **버킷 삭제** 클릭

### 4-3. VPC 보안 그룹 삭제

#### 4-3-1. 커스텀 보안 그룹 삭제
1. **EC2 콘솔** → **네트워크 및 보안** → **보안 그룹**
2. `rds-postgresql-sg` 선택
3. **작업** → **보안 그룹 삭제** 클릭
4. **삭제** 클릭

> **📌 참고**: RDS 인스턴스가 완전히 삭제된 후에만 보안 그룹 삭제가 가능하다.

### 4-4. IAM 리소스 정리 (보안 권장)

#### 4-4-1. 액세스 키 비활성화/삭제
1. **IAM 콘솔** → **사용자** → 생성한 사용자 클릭
2. **보안 자격 증명** 탭 → **액세스 키** 섹션
3. 생성한 액세스 키 → **비활성화** 클릭 (또는 **삭제**)

#### 4-4-2. IAM 사용자 삭제 (선택사항)
1. **IAM 콘솔** → **사용자** → 생성한 사용자 선택
2. **삭제** 클릭
3. **확인**: 사용자명 입력
4. **삭제** 클릭

### 4-5. 최종 확인 체크리스트

실습 종료 후 다음 항목들이 모두 삭제되었는지 확인:

- [ ] **RDS 인스턴스 삭제 완료**
  - [ ] `menu-management-db` 인스턴스 삭제됨
  - [ ] RDS 대시보드에서 인스턴스 목록 비어있음 확인
- [ ] **S3 리소스 삭제 완료**
  - [ ] S3 버킷 내 모든 객체 삭제됨
  - [ ] S3 버킷 자체 삭제됨
  - [ ] S3 대시보드에서 버킷 목록 확인
- [ ] **VPC 보안 그룹 삭제 완료**
  - [ ] `rds-postgresql-sg` 보안 그룹 삭제됨
- [ ] **IAM 리소스 정리 완료**
  - [ ] 액세스 키 비활성화/삭제됨
  - [ ] IAM 사용자 삭제됨 (선택사항)

### 4-6. 과금 확인 방법

#### 4-6-1. 실시간 비용 모니터링
1. **결제 및 비용 관리** → **Cost Explorer** 이동
2. **일별 비용** 확인
3. **서비스별 비용** 확인하여 RDS, S3 과금 여부 점검

#### 4-6-2. 청구서 확인
- 리소스 삭제 후 1-2일 뒤 **결제 및 비용 관리** → **청구서**에서 최종 과금 확인
- 프리 티어 사용량도 함께 확인

> **💡 팁**: 실습 중 예상치 못한 과금을 방지하려면 **결제 알림**을 $1-5 정도로 낮게 설정하는 것을 권장한다.

---

## 체크포인트

Phase 1 전체 완료 후 확인사항:
- [ ] **IAM 설정**
  - [ ] Root 계정 MFA 활성화 완료
  - [ ] 결제 알림 설정 완료
  - [ ] IAM 사용자 생성 및 정책 연결 완료
  - [ ] 액세스 키 생성 및 안전한 저장 완료
- [ ] **S3 설정**
  - [ ] S3 버킷 생성 완료
  - [ ] 퍼블릭 액세스 및 CORS 설정 완료
  - [ ] 기존 이미지 S3 업로드 완료
- [ ] **RDS 설정**
  - [ ] VPC 보안 그룹 생성 완료
  - [ ] PostgreSQL RDS 인스턴스 생성 완료
  - [ ] 데이터베이스 연결 확인 완료
  - [ ] 스키마 및 초기 데이터 생성 완료

**중요한 정보 기록:**
- IAM 액세스 키 ID: `___________________`
- IAM 시크릿 액세스 키: `___________________`
- S3 버킷명: `___________________`
- RDS 엔드포인트: `___________________`
- RDS 마스터 비밀번호: `___________________`

---

**다음 단계**: Phase 2 - Spring Boot 애플리케이션 AWS 환경 대응
