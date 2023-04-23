FROM adoptopenjdk/openjdk14:alpine-slim

WORKDIR /app
COPY target/dataportal-backend-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "--server.port=${PORT}", "--spring.datasource.url=${DATAPORTAL_PORTAL_PROD_URL}", "spring.datasource.username=${DATAPORTAL_PROD_USER}", "spring.datasource.password=${DATAPORTAL_PROD_PASSWORD}"]
