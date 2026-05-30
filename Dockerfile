FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY code-source/bibliotheque/.mvn/ .mvn/
COPY code-source/bibliotheque/mvnw .
COPY code-source/bibliotheque/pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline
COPY code-source/bibliotheque/src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/*.jar"]
