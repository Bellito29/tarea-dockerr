FROM maven:3.9.11-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true -B

EXPOSE 8081

CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.war"]
