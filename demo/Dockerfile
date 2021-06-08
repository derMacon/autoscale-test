#pull base image
FROM openjdk:11-jre-slim-buster

#default command
CMD java -jar /data/stack-demo.jar

#copy jar to docker image
ADD ./target/stack-demo.jar /data/stack-demo.jar
