FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/proyecto-final-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8080
RUN echo $BACKED_USERNAME
RUN echo $BACKEND_PASSWORD
RUN echo $BACKED_DB
CMD ["java", "-jar","backend.jar"]