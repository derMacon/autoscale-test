#pull base image
FROM openjdk:11-jre-slim-buster

#default command
CMD java -jar /data/supplier-backend.jar

#copy jar to docker image
ADD ./target/supplier-backend.jar /data/supplier-backend.jar
