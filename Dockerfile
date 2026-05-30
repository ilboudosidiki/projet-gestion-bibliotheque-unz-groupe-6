FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copier les fichiers Maven
COPY code-source/bibliotheque/.mvn/ .mvn/
COPY code-source/bibliotheque/mvnw .
COPY code-source/bibliotheque/pom.xml .

# Rendre mvnw executable
RUN chmod +x mvnw

# Telecharger les dependances
RUN ./mvnw dependency:go-offline -B

# Copier le code source
COPY code-source/bibliotheque/src ./src

# Compiler
RUN ./mvnw clean package -DskipTests -B

# Verifier que le jar existe et le lancer
CMD java -jar $(ls target/*.jar | head -1)
