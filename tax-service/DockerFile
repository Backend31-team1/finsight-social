FROM gradle:7.6-jdk17 AS build

WORKDIR /home/gradle/project

COPY . .

RUN ./gradlew :tax-service:clean :tax-service:build -x test

# -----

FROM eclipse-temurin:17-jdk-jammy AS runtime

WORKDIR /app

COPY --from=build /home/gradle/project/tax-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
