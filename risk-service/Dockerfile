FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar risk-service.jar
ENTRYPOINT ["java","-jar","/risk-service.jar"]
EXPOSE 8083

