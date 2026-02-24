# 1) Build stage
FROM amazoncorretto:17-alpine AS builder
WORKDIR /app

# 프로젝트 정보 (jar 이름 정규화에 사용)
ARG PROJECT_NAME=discodeit
ARG PROJECT_VERSION=1.2-M8

# 1) Gradle wrapper/설정만 먼저 복사 (의존성 캐시 레이어 고정)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# 권한
RUN chmod +x ./gradlew

# 2) 의존성만 먼저 받아 캐시(소스 변경과 분리)
RUN ./gradlew --no-daemon dependencies

# 3) 그 다음에 소스 복사 (자주 변경되는 부분)
COPY src ./src

# 4) 빌드 (테스트는 필요 시 -x test)
RUN ./gradlew --no-daemon clean bootJar

# 5) jar 이름 정규화 (패턴이 여러 개여도 1개 선택)
RUN set -eux; \
    JAR_PATH="$(ls -1 build/libs/*.jar | head -n 1)"; \
    cp "$JAR_PATH" "/app/${PROJECT_NAME}-${PROJECT_VERSION}.jar"

# 2) Runtime stage
FROM amazoncorretto:17-alpine AS runtime
WORKDIR /app

ENV JVM_OPTS=""

EXPOSE 80

# 빌드 결과물만 복사
ARG PROJECT_NAME=discodeit
ARG PROJECT_VERSION=1.2-M8
COPY --from=builder /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar /app/app.jar

# 실행
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app/app.jar"]