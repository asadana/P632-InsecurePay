#!/usr/bin/bash

command -v docker >/dev/null 2>&1 || { 
	echo >&2 "This script requires docker to be installed."; 
	echo "Please install docker and try again.";
	echo "Script will exit now...";
	exit 1; 
}

# tomcatPortNo value points to the host port number assigned to the docker
echo -n "Enter the port you want to assign to tomcat container [default 8080]: "
read tomcatPortNo

numberRegex='^[0-9]+$'
if [[ ( -z $tomcatPortNo ) || ( ! $tomcatPortNo =~ $numberRegex ) ]]; then	
	echo "Using default port for tomcat container"
	tomcatPortNo=8080
	exit 1;
fi

tomcatName="insecurepay/tomcat:v1"
tomcatContainerName="tomcat"

postgresName="insecurepay/postgres:v1" 
# NOTE: If you change the postgresContainerName here, 
# then you need to change it in InsecurePayServiceServer com/cigital/common/Constants.java
# and repack the war file docker/dockerTomcat/InsecurePayServiceServer.war
postgresContainerName="postgres"

cd ./dockerTomcat
echo "Building Tomcat image: $tomcatName"
echo
docker build -t "$tomcatName" .

cd ./../dockerPostgres
echo "Building Postgres image: $postgresName"
echo
docker build -t "$postgresName" .

cd ./../

# Check if container already exists, and remove it
tomcatContainerID="$(docker ps -a | grep "\s$tomcatContainerName$" | grep -Eo '^[^ ]+')"
postgresContainerID="$(docker ps -a | grep "\s$postgresContainerName$" | grep -Eo '^[^ ]+')"

if [[ ! -z $tomcatContainerID ]]; then
	echo
	echo "===== Removing $tomcatContainerName ====="
	docker rm -f "$tomcatContainerID"
fi
if [[ ! -z $postgresContainerID ]]; then
	echo
	echo "===== Removing $postgresContainerName ====="
	docker rm -f "$postgresContainerID"
fi


echo
echo "===== Starting $postgresContainerName container ====="
docker run -d --name "$postgresContainerName" "$postgresName"
echo 
echo "===== Starting $tomcatContainerName container ====="
docker run \
		-d \
		-p "$tomcatPortNo":8080 \
		--name="$tomcatContainerName" \
		--link="$postgresContainerName":"$postgresContainerName" \
		"$tomcatName"
