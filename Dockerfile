# 1) Build stage
FROM amazoncorretto:17-alpine AS builder
WORKDIR /app

# 요구사항: 프로젝트 정보를 환경 변수로 설정(jar 이름 추론에 사용)
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8

# 프로젝트 파일 복사
COPY . .

# 빌드
RUN chmod +x gradlew
RUN ./gradlew clean bootJar

# jar 이름 정규화
RUN set -eux; \
    JAR_PATH="$(ls -1 build/libs/*.jar | head -n 1)"; \
    cp "$JAR_PATH" "build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"


# 2) Runtime stage
FROM amazoncorretto:17-alpine
WORKDIR /app

# 요구사항: JVM 옵션 환경 변수(기본 빈 문자열)
ENV JVM_OPTS=""

# 요구사항: 프로젝트 정보 환경 변수
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8

# 요구사항: 80 포트 노출
EXPOSE 80

# jar 복사
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar /app/

# 80으로 실제 리슨하려면 아래 중 하나가 필요:
# -e SERVER_PORT=80 또는 application-aws.yml에 server.port: 80
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
