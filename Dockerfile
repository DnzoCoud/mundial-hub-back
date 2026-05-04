FROM eclipse-temurin:21-jdk
RUN apt-get update && apt-get install -y curl

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

EXPOSE 8080