#!/bin/bash

cd ../
docker build -t node-consumer:latest .
cd ../
docker stop node-consumer
docker rm node-consumer
docker-compose up -d node-consumer
docker logs node-consumer -f
# docker exec -it node-consumer sh