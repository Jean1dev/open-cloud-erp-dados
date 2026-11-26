FROM eclipse-temurin:25-jdk AS builder

WORKDIR /usr/app/

COPY . .

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jre

COPY --from=builder /usr/app/build/libs/*.jar /opt/app/application.jar

RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

ENV PORT 8080
ENV MONGO_URL mongodb://172.18.0.1:27017/openerp
EXPOSE 8080

CMD ["java", "-jar", "/opt/app/application.jar"]
