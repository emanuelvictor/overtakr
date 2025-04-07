LABEL maintainer="Overtakr <contact@overtakr.com>"
LABEL org.opencontainers.image.authors="Overtakr <contact@overtakr.com>"

# ----------------------------------------
# Stage 1: Clone repository (Git)
# ----------------------------------------
FROM alpine/git AS git-clone
WORKDIR /app
RUN git clone https://github.com/emanuelvictor/overtakr

# ----------------------------------------
# Stage 2: Build of project (Maven)
# ----------------------------------------
FROM maven:3.9.4-eclipse-temurin-8 AS build
WORKDIR /app

# Copy the cloned code
COPY --from=git-clone /app/overtakr /app/overtakr

# Enter in directory of module and make the build
WORKDIR /app/overtakr/api/non-functional/discovery
RUN mvn clean package -DskipTests

# ----------------------------------------
# Stage 3: Execution (JDK)
# ----------------------------------------
FROM eclipse-temurin:8-jdk-alpine
WORKDIR /app

# Copy only the jar generated
COPY --from=build /app/overtakr/api/non-functional/discovery/target/*.jar app.jar

# Expose port
EXPOSE 8761

# Commando to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]