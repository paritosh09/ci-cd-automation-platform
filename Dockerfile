FROM openjdk:17-jdk-slim as builder

WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jre-slim

RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN chown appuser:appuser app.jar

USER appuser

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
