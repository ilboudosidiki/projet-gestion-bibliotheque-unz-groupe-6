FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY code-source/bibliotheque/pom.xml .
COPY code-source/bibliotheque/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "java -jar app.jar --spring.datasource.url=$SPRING_DATASOURCE_URL --spring.datasource.username=$SPRING_DATASOURCE_USERNAME --spring.datasource.password=$SPRING_DATASOURCE_PASSWORD --app.jwt.secret=$APP_JWT_SECRET"]
