#!/usr/bin/bash

command -v docker >/dev/null 2>&1 || { 
	echo >&2 "This script requires docker to be installed."; 
	echo "Please install docker and try again.";
	echo "Script will exit now...";
	exit 1; 
}


cd ./dockerTomcat
tomcatName="insecurepay/tomcat:v1"
echo "Building Tomcat image: $tomcatName"
echo
docker build -t "$tomcatName" .

cd ./../dockerPostgres
postgresName="insecurepay/postgres:v1"
echo "Building Postgres image: $postgresName"
echo
docker build -t "$postgresName" .

cd ./../
# TODO: Add a condition to check if container already exists, with exact name, and remove it
# docker rm $(docker ps -a -q --filter="name=tomcat")
echo
echo "===== Starting postgres container ====="
docker run -d --name postgres insecurepay/postgres:v1
echo 
echo "===== Starting tomcat container ====="
docker run -d -p 8080:8080 --name tomcat --link postgres:postgres insecurepay/tomcat:v1
