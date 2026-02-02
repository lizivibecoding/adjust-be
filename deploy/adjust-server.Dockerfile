FROM eclipse-temurin:21-jre

WORKDIR /app

# The jar is delivered by CI to the build context directory
COPY adjust-server.jar app.jar

EXPOSE 48080

ENTRYPOINT ["java","-Xms512m","-Xmx512m","-jar","/app/app.jar","--spring.profiles.active=local"]

