FROM maven:3.6.3-openjdk-11-slim

COPY pom.xml .
COPY src/ /src/

RUN mvn package

WORKDIR target
ENTRYPOINT ["java", "-jar", "Bookit-1.0-SNAPSHOT.jar"]
