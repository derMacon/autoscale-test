#!/bin/bash

echo 'remove development stack if existent'
docker-compose -f docker-compose_dev.yml down -v

echo 'parse prometheus alert rule config'
export $(cat .env | xargs)
rm data/prometheus/alert.yml
./scripts/prod/shell_expension.sh data/prometheus/alert-unparsed.yml data/prometheus/alert.yml

# https://stackoverflow.com/questions/44694640/docker-swarm-with-image-versions-externalized-to-env-file
echo 'use docker-compose as preprocessor for environmental variables'
docker-compose config > docker-compose-parsed.yaml

echo 'init swarm'
docker swarm init

echo 'redeploy stack'
docker stack rm vossibility
docker stack deploy --compose-file docker-compose-parsed.yaml vossibility
