#!/bin/bash

echo 'direct scaling benchmarks'

PROCESSED_MSG_CNT=1;
OVERALL_MSG_CNT=$(( 2 * $1 * $2 ))

request() {
	echo "$PROCESSED_MSG_CNT/$OVERALL_MSG_CNT - scaling request: {scalingOffset: $1, repetition: $2/$3, service: $4}"
	curl "http://localhost:9245/manual-scale?additionalCnt=$1&service=$4"
}

node_request() {
	request $1 $2 $3 'NODE'
}

spring_request() {
	request $1 $2 $3 'SPRING'
}

for scalingOffset in $(seq 1 $1)
do 
	for curr_rep in $(seq 1 $2)
	do 
		node_request $scalingOffset $curr_rep $2
		PROCESSED_MSG_CNT=$(( $PROCESSED_MSG_CNT + 1 ))
		spring_request $scalingOffset $curr_rep $2
		PROCESSED_MSG_CNT=$(( $PROCESSED_MSG_CNT + 1 ))
	done
done
