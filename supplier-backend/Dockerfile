##pull base image
#FROM openjdk:11-jre-slim-buster
#
##default command
#CMD java -jar /data/supplier-backend.jar
#
##copy jar to docker image
#ADD ./target/supplier-backend.jar /data/supplier-backend.jar


# https://stackoverflow.com/questions/27767264/how-to-dockerize-maven-project-and-how-many-ways-to-accomplish-it
FROM maven:3.8.1-openjdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM gcr.io/distroless/java
COPY --from=build /usr/src/app/target/supplier-backend.jar /usr/app/supplier-backend.jar
EXPOSE 9245
ENTRYPOINT ["java","-jar","/usr/app/supplier-backend.jar"]
