# Etapa 1: Imagem com Maven e Git para buildar o projeto
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Diretório de trabalho
WORKDIR /app

# Clona o repositório (substitua pela sua URL)
RUN git clone https://github.com/usuario/repositorio.git .

# Faz o build do projeto com Maven
RUN mvn clean package -DskipTests

# Etapa 2: Imagem leve só com JDK para rodar o app
FROM eclipse-temurin:17-jdk-alpine

# Cria diretório de trabalho
WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
