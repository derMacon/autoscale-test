#pull base image
FROM openjdk:11-jre-slim-buster

#default command
CMD java -jar /data/receiver-demo.jar

#copy jar to docker image
ADD ./target/receiver-demo.jar /data/receiver-demo.jar
