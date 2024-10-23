FROM maven:3.9.9-eclipse-temurin-21-jammy AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21

WORKDIR /app

COPY --from=build /app/target/sbs-0.0.1-SNAPSHOT.jar /app/sbs.jar

ENTRYPOINT ["java", "-jar", "/app/sbs.jar"]
