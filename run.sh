#!/bin/bash

echo 'create network'
docker network create scaler

docker-compose build

./parseStackConfig.sh

