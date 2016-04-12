#!/bin/sh
# 
# postInstall.sh
#
# This script is executed after the installation of Tomcat/PostgreSQL.
# Here we set up blah blah blah.
#
# Project Name: InsecurePay
#
# Authors: Amish (shah7), Ankit (asadana), Jaini (voraj),
#		   Janak (jbhalla), Madhukiran (madhradh)
# 

sampleFunction() {
	echo ========================
}

sampleFunction
echo "This is executed by the shell script"
echo "la la la la"

sampleFunction
echo "This is your entry directory: "
pwd

sampleFunction
echo "This is the current list of files here: "
ls -al --color=auto

sampleFunction
echo "Your user name: "
whoami

# examples

# datalist='amish ankit jaini janak madhukiran'
# for data in $datalist
# 	do
# 	echo $data
# done

# for (( i = 0; i < 10; i++ ))
# 	do
# 		echo $i
# done

## if inside for loop
# for value in {1..5}
# 	do
#  	#echo "Equal to three
# 	if [ $value -eq 3 ]; then
# 		echo $value
# 	else 
# 		# Greater than equal to four
# 		if [ $value -ge 4 ]; then
# 			echo $value
# 		else
# 			#Greater or equal to 1 or less than 3
# 			if [ $value -ge 1 ] || [ $value -lt 3 ]; then
# 				echo $value
# 			fi
# 		fi
# 	fi 		
# done

# for some in {10..0..2}
# 	do
# 		echo $some
# done