FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY code-source/bibliotheque/pom.xml .
COPY code-source/bibliotheque/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/bibliotheque-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV SPRING_DATASOURCE_URL=""
ENV SPRING_DATASOURCE_USERNAME=""
ENV SPRING_DATASOURCE_PASSWORD=""
ENV APP_JWT_SECRET=""
CMD java -jar app.jar
