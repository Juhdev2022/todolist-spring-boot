# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas os arquivos necessários para o build
COPY pom.xml .
COPY src ./src

# Build da aplicação (skip tests para build mais rápido)
RUN mvn clean package -DskipTests

# Verifica se o JAR foi criado
RUN ls -la /app/target/*.jar || (echo "JAR não encontrado!" && exit 1)

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR do stage de build (usa wildcard para pegar qualquer JAR)
COPY --from=build /app/target/todolist-*.jar app.jar

# Expõe a porta (Render usa variável PORT automaticamente)
EXPOSE 8080

# Comando para executar a aplicação
# Render injeta a variável PORT automaticamente via application.properties
ENTRYPOINT ["java", "-jar", "app.jar"]
