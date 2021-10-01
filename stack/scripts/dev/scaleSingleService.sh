#!/bin/bash

HOST=10.21.2.221

echo "scaling specific service $1 by $2" 
curl "http://$HOST:9245/manual-scale?additionalCnt=$2&service=$1"

