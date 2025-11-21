
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app


COPY pom.xml .
RUN mvn -q dependency:go-offline


COPY src ./src
RUN mvn -q clean package -DskipTests


FROM eclipse-temurin:17-jdk

WORKDIR /app


ENV PORT=8080
EXPOSE 8080


COPY --from=build /app/target/*.jar app.jar


ENTRYPOINT ["java", "-jar", "app.jar"]