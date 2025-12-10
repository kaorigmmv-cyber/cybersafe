FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/proyecto-0.0.1-SNAPSHOT.war app.war

EXPOSE 8080

CMD ["java", "-jar", "app.war"]