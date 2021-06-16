#!/bin/bash

printf "\
 +-------------------+
 | TRAFFIC GENERATOR |
 +-------------------+
  -> Benchmark tests
  -> version: v1.0

"

for file in $(find ./ -name '*.benchmark'); do
  echo "send request: $file"
  ./curl-benchmark.sh $file
done
