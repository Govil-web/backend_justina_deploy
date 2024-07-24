FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

COPY target/justina-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]



