#pull base image
FROM openjdk:11-jre-slim-buster

#default command
CMD java -jar /data/scaler-proxy.jar

#copy jar to docker image
ADD ./target/scaler-proxy.jar /data/scaler-proxy.jar
