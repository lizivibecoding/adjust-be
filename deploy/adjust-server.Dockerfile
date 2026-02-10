FROM eclipse-temurin:21-jre

WORKDIR /app

# Timezone: make both OS and JVM use Asia/Shanghai (UTC+8)
ENV TZ=Asia/Shanghai
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=Asia/Shanghai"

# The jar is delivered by CI to the build context directory
COPY adjust-server.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE="local"
ENV SERVER_PORT="48080"
ENV LOG_PATH="/app/logs"

RUN mkdir -p /app/logs

EXPOSE 48080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE} --server.port=${SERVER_PORT}"]

