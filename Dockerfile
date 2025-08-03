FROM eclipse-temurin:21-jdk AS builder

WORKDIR /builder

COPY gradlew ./

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

RUN ./gradlew dependencies

COPY . .

RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /builder/build/libs/*.jar app.jar


ENTRYPOINT ["java", "-jar", "app.jar"]
