FROM openjdk:17.0.1-jdk-slim
WORKDIR /app

# Install the tzdata package and set the timezone
RUN apt-get update && apt-get install -y tzdata
ENV TZ=Europe/Athens

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

EXPOSE 8443

CMD ["./mvnw", "spring-boot:run"]
