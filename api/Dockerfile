FROM openjdk:23
WORKDIR /app
COPY target/api-0.0.1-SNAPSHOT.jar api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker" ,"api-0.0.1-SNAPSHOT.jar"]