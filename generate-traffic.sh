#!/bin/bash

printf "\
 +-------------------+
 | TRAFFIC GENERATOR |
 +-------------------+
  -> Benchmark tests
  -> version: v1.0

"

for file in $(find ./request-scripts -name '*.benchmark'); do
  echo "send request: $file"
  ./request-scripts/curl-benchmark.sh $file
done
