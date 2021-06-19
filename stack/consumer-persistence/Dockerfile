# https://stackoverflow.com/questions/27767264/how-to-dockerize-maven-project-and-how-many-ways-to-accomplish-it
FROM maven:3.8.1-openjdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM gcr.io/distroless/java
COPY --from=build /usr/src/app/target/consumer-persistence.jar /usr/app/consumer-persistence.jar
EXPOSE 7143
ENTRYPOINT ["java","-jar","/usr/app/consumer-persistence.jar"]
