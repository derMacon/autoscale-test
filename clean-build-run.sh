#!/bin/bash


STACK_DIR=stack
export $(cat $STACK_DIR/.env | xargs)

echo 'create network'
docker network create -d overlay scaler

echo 'build images'
docker-compose -f $STACK_DIR/docker-compose.yml build

./start-stack-services.sh

