#!/bin/bash

# https://stackoverflow.com/questions/43053013/how-do-i-check-that-a-docker-host-is-in-swarm-mode
function isSwarmNode(){
    if [ "$(docker info | grep Swarm | sed 's/Swarm: //g')" == "inactive" ]; then
        echo false;
    else
        echo true;
    fi
}


STACK_DIR=stack
echo 'remove development stack if existent'
docker-compose -f $STACK_DIR/docker-compose_dev.yml down -v
docker-compose -f $STACK_DIR/docker-compose_quarkus.yml down -v

echo 'parse prometheus alert rule config'
export $(cat $STACK_DIR/.env | xargs)
rm $STACK_DIR/data/prometheus/alert.yml
$STACK_DIR/scripts/prod/shell_expension.sh $STACK_DIR/data/prometheus/alert-unparsed.yml $STACK_DIR/data/prometheus/alert.yml

# https://stackoverflow.com/questions/44694640/docker-swarm-with-image-versions-externalized-to-env-file
echo 'use docker-compose as preprocessor for environmental variables'
docker-compose -f $STACK_DIR/docker-compose.yml config > $STACK_DIR/docker-compose-parsed.yaml

if [ ! isSwarmNode ]; then
	echo 'init swarm'
	docker swarm init
fi

echo 'redeploy stack'
docker stack rm vossibility
docker stack deploy --compose-file $STACK_DIR/docker-compose-parsed.yaml vossibility
