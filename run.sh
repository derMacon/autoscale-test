#!/bin/bash

echo 'create network'
docker network create -d overlay scaler

docker-compose build

./parseStackConfig.sh

