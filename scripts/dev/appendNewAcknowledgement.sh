#!/bin/bash

curl -XPOST -u admin:admin -d "body=message" http://localhost:8161/api/message/ackqueue?type=queue

