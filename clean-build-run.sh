#!/bin/bash

echo 'create network'
docker network create -d overlay scaler

echo 'build images'
docker-compose -f stack/docker-compose.yml build

./start-stack-services.sh

