#!/bin/bash

docker-compose -f docker-compose_dev.yml down -v

# parse prometheus alert rule config
export $(cat .env | xargs)
rm data/prometheus/alert.yml
./scripts/prod/shell_expension.sh data/prometheus/alert-unparsed.yml data/prometheus/alert.yml

# use docker-compose as preprocessor for environmental variables
# https://stackoverflow.com/questions/44694640/docker-swarm-with-image-versions-externalized-to-env-file
docker-compose config > docker-compose-parsed.yaml

docker swarm init

docker stack rm vossibility
docker stack deploy --compose-file docker-compose-parsed.yaml vossibility
