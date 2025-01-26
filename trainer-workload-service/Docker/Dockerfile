FROM openjdk:21-jdk-slim
WORKDIR /app
COPY trainer-workload-service-1.0.jar report-app.jar
CMD ["java", "-jar", "report-app.jar"]
EXPOSE 8082