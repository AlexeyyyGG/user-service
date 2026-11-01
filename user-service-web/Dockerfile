FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -pl user-service-web -am

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/user-service-web/target/user-service-web-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]