#!/bin/bash

RAND_ID="$(openssl rand -base64 12)"
echo "new msg with random id: $RAND_ID to queue: $1"
curl -XPOST -u admin:admin -d "body=$RAND_ID" http://localhost:8161/api/message/$1ack?type=queue
