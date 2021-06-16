#!/bin/bash

TMP="$(docker ps | grep "$1" | grep -Eo '^[^ ]+')"
echo $TMP
docker logs $TMP
