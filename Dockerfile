FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven

RUN mvn clean package -DskipTests

COPY target/*.war app.war

EXPOSE 8080

CMD ["java", "-jar", "app.war"]