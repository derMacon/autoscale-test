#!/bin/bash

echo 'build supplier-backend'
mvn -f supplier-backend/pom.xml clean package -DskipTests

echo 'build scaler proxy'
mvn -f scaler-proxy/pom.xml clean package -DskipTests

echo 'remove any old stack'
docker stack rm vossibility

echo 'create network'
docker network create scaler

echo 'use docker-compose as preprocessor for environmental variables'
# https://stackoverflow.com/questions/44694640/docker-swarm-with-image-versions-externalized-to-env-file
docker-compose config > docker-compose-parsed.yaml

echo 'start docker compose stack'
docker stack deploy --compose-file docker-compose-parsed.yaml vossibility

