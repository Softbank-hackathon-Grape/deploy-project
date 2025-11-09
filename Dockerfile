FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src ./src

RUN chmod +x backend/gradlew

RUN backend/gradlew -p backend clean build -x test --no-daemon

# 단일 JAR로 이름 통일
RUN JAR_PATH=$(ls backend/build/libs/*.jar | head -n 1) \
    && cp "$JAR_PATH" /workspace/app.jar

# ---- Runtime stage ----------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# 타임존/유저/헬스체크 준비
ENV TZ=Asia/Seoul \
    JAVA_OPTS="" \
    SERVER_PORT=9000

RUN apk add --no-cache tzdata curl \
    && addgroup -S spring && adduser -S spring -G spring

COPY --from=builder /workspace/app.jar /app/app.jar

USER spring:spring
EXPOSE 9000

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dserver.port=${SERVER_PORT} -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]
