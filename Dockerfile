# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas os arquivos necessários para o build
COPY pom.xml .
COPY src ./src

# Build da aplicação (skip tests para build mais rápido)
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app

# Cria usuário não-root para segurança
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copia o JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta (pode ser sobrescrita pela variável PORT)
EXPOSE 8080

# Variável de ambiente para porta (compatível com application.properties)
ENV PORT=8080

# Healthcheck para verificar se a aplicação está rodando
# Usa curl que está disponível na imagem base
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:${PORT}/api/tarefas || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT} app.jar"]
