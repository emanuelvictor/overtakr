# Etapa 1: Imagem com Maven e Git para buildar o projeto
FROM maven:3.9.4-eclipse-temurin-8 AS build

WORKDIR usr/src/app

COPY . ./

RUN mvn clean package -DskipTests

# Etapa 2: Imagem leve só com JDK para rodar o app
FROM eclipse-temurin:8-jdk-alpine

ARG JAR_NAME="discovery-1.0.0-SNAPSHOT"

# Cria diretório de trabalho
WORKDIR /usr/src/app

# Copia o JAR gerado na etapa anterior
COPY --from=build /usr/src/app/target/${JAR_NAME}.jar ./app.jar

# Expõe a porta padrão do Spring
EXPOSE 8761

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
