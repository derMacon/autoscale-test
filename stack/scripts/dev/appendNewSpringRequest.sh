#!/bin/bash

count=28
for i in $(seq $count); do
curl -XPOST -u admin:admin -d "body=message" http://localhost:8161/api/message/springqueue?type=queue
done

