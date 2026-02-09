FROM eclipse-temurin:21-jre

WORKDIR /app

# The jar is delivered by CI to the build context directory
COPY adjust-server.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE="local"
ENV SERVER_PORT="48080"

EXPOSE 48080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE} --server.port=${SERVER_PORT}"]

