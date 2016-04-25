#!/usr/bin/bash

echo

# Check to see if docker is installed
command -v docker >/dev/null 2>&1 || { 
	echo >&2 "This script requires docker to be installed."; 
	echo "Please install docker and try again.";
	echo "Script will exit now...";
	exit 1; 
}

tomcatName="insecurepay/tomcat:v1"
tomcatContainerName="tomcat"

postgresName="insecurepay/postgres:v1" 

# NOTE: If you change the postgresContainerName here, 
# then you need to change it in InsecurePayServiceServer com/cigital/common/Constants.java
# and repack the war file docker/dockerTomcat/InsecurePayServiceServer.war
postgresContainerName="postgres"

while [[ true ]]; do
	# tomcatPortNo value points to the host port number assigned to the docker
	echo -n "Enter the port you want to assign to $tomcatContainerName container [default 8080]: "
	read tomcatPortNo
	echo

	# Check if entered value is empty or non-numeric, then set value to default
	numberRegex='^[0-9]+$'
	if [[ ( -z $tomcatPortNo ) || ( ! $tomcatPortNo =~ $numberRegex ) ]]; then	
		echo "No valid port number entered. Using default port."
		tomcatPortNo=8080
	fi

	echo
	echo "Checking port availability..."
	checkPort=$(netstat -ln | grep ":$tomcatPortNo")
	# Check if the port is already in use
	if [[ ! -z "$checkPort" ]]; then
			echo
			echo "Port is already in use."
			echo "$checkPort"
			echo
			echo "Continue anyway? (y/n): "
			read -n 1 yesNoReply
			if [[ ($yesNoReply == "y") || ($yesNoReply == "Y") ]]; then
				echo
				break;
			fi

		else
			echo "Port is currently not in use."
			break;
	fi
	
done
echo "Using port: $tomcatPortNo for $tomcatContainerName container"

# Building tomcat image
cd ./dockerTomcat
echo
echo "===== Building Tomcat image: $tomcatName ====="
docker build -q -t "$tomcatName" .
echo

# Building postgres image
cd ./../dockerPostgres
echo "===== Building Postgres image: $postgresName ====="
docker build -q -t "$postgresName" .
echo

cd ./../

# Check if container already exists, and remove it
tomcatContainerID="$(docker ps -a | \
					grep "\s$tomcatContainerName$" | grep -Eo '^[^ ]+')"
postgresContainerID="$(docker ps -a | \
					grep "\s$postgresContainerName$" | grep -Eo '^[^ ]+')"

if [[ ! -z $tomcatContainerID ]]; then
	echo "===== Removing existing $tomcatContainerName container ====="
	docker rm -f "$tomcatContainerID"
	echo
fi
if [[ ! -z $postgresContainerID ]]; then
	echo "===== Removing existing $postgresContainerName container ====="
	docker rm -f "$postgresContainerID"
	echo
fi

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

echo
echo "===== Displaying insecurepay containers ====="
docker ps -a | grep "insecurepay"
echo
echo "===== Exiting runDocker script ====="