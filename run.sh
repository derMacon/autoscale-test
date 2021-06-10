#!/bin/bash

echo 'build supplier-backend'
mvn -f supplier-backend/pom.xml clean package -DskipTests

echo 'build scaler proxy'
mvn -f scaler-proxy/pom.xml clean package -DskipTests

echo 'create network'
docker network create scaler

./parseStackConfig.sh

