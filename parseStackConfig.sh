#!/bin/bash

docker-compose build

# use docker-compose as preprocessor for environmental variables
# https://stackoverflow.com/questions/44694640/docker-swarm-with-image-versions-externalized-to-env-file
docker-compose config > docker-compose-parsed.yaml

docker stack rm vossibility
docker stack deploy --compose-file docker-compose-parsed.yaml vossibility
