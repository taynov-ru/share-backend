FROM eclipse-temurin:21-jdk-alpine
LABEL name="share-backend"
EXPOSE 8080

WORKDIR /app

COPY *.jar ./app.jar

CMD ["java", "-jar", "app.jar" ]
