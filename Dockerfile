FROM openjdk:11-jre-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENV PORT 8080
ENV MONGO_URL mongodb://localhost:27017/openerp

ENTRYPOINT ["java","-jar","/app.jar"]