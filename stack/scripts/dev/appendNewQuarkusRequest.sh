#!/bin/bash

count=1
for i in $(seq $count); do
curl -XPOST -u admin:admin -d "body=message" http://localhost:8161/api/message/quarkusqueue?type=queue
done

