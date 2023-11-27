FROM openjdk:11-jdk-slim
WORKDIR /app
ARG JAR_FILE=bitgouel-api/build/libs/*.jar
COPY ${JAR_FILE} bitgouel-api.jar
ENV TZ=Asia/Seoul
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "bitgouel-api.jar"]