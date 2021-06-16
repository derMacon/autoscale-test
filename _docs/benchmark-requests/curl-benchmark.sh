#!/bin/bash


usage() {
(echo "Usage:
To inspect the grammar of the requests, see the readme.md in the same directory
curl-benchmark.sh -h | curl-benchmark.sh --help
  prints this help and exits
curl-benchmark.sh [INPUT]
  where INPUT is the .benchmark request file")
}

sendRequest() {
	export $(cat .env | xargs)
	URL="http://$API_HOST:$API_PORT/api/v1/parser/benchmark"

	REQUEST="$( cat $1 )"

  printf "  sending benchmark request to
  - url: $URL
  - req: $REQUEST
  To view the metrics visit the grafan client on the host machine (port 3000)" 

	curl -X POST -H "Content-Type: text/plain" --data "$REQUEST" $URL
}

if [ $# -eq 0 ] || [ $# -gt 1 ]; then
	usage
	exit 0
fi

case $1 in
	--help)
		;&
	-h)
		usage
		;;
	
	*)
		sendRequest $1
		;;
esac


