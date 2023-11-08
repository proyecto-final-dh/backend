FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/proyecto-final-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8080
# CMD ["/bin/echo",  $BACKEND_PASSWORD]
# CMD ["/bin/echo",   $BACKED_DB]
# CMD ["java", "-jar","backend.jar"]
CMD ["/bin/echo", $BACKED_USERNAME]