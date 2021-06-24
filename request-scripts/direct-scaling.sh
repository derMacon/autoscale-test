#!/bin/bash

echo 'direct scaling benchmarks'

request() {
	echo "do request with $1"
}

for (( i=0; i<=$1; i++ ))
do 
	request $i
done