FROM --platform=linux/aarch64 amazoncorretto:17
COPY target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]