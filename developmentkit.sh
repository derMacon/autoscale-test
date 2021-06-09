#!/bin/bash

usage() {
(echo "Usage:
developmentkit.sh -h | developmentkit.sh --help
  prints this help and exits
developmentkit.sh [OPTION]
  OPTIONS ARG
    -e | --export [TAR-FILE]
    -i | --import [TAR-FILE]")
}


import_image() {
	echo "import .tar ($1) to image"
	for arg in "$@"
	do
		if [ -f $arg ]; then
			docker load --input $arg 
		else
			echo "tar file not available"
		fi

	done
}


export_image() {

	for arg in "$@"
	do
		echo "save image: ${arg}"
		docker save $arg > ./$arg.tar
	done

}


case $1 in

	--import)
		;&
	-i)
		import_image ${@:2}
		;;

	--export)
		;&
	-e)
		export_image ${@:2}
		;;


	*)
		usage
		;;

esac
