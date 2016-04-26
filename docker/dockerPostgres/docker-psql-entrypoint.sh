#!/bin/bash
chown -R postgres "$PGDATA"

# Check if the PGDATA path provided is null/empty
if [ -z "$(ls -A "$PGDATA")" ]; then

	# Initializing the database cluster in $PGDATA
    gosu postgres initdb

    # Setting the database cluster to listen to all addresses.
    sed -ri "s/^#(listen_addresses\s*=\s*)\S+/\1'*'/" "$PGDATA"/postgresql.conf

    # Checking if the password value is set,
    # then set the pass variable.
    if [ "$POSTGRES_PASSWORD" ]; then
      pass="PASSWORD '$POSTGRES_PASSWORD'"
      authMethod=md5
    else
      echo "==============================="
      echo "!!! Use \$POSTGRES_PASSWORD env var to secure your database !!!"
      echo "==============================="
      pass=
      authMethod=trust
    fi
    echo
	
	# If the username provided is not postgres then
	# use create command
    if [ "$POSTGRES_USERNAME" != 'postgres' ]; then
      op=CREATE
    else
      op=ALTER
    fi

    # Creating/Altering the user with superuser access and provided password
    userSql="$op USER $POSTGRES_USERNAME WITH SUPERUSER $pass;"
    echo $userSql | gosu postgres postgres --single -jE
    echo


	# If the database is not the default postgres, then 
	# create the provide database with the new user we created 
    if [ "$POSTGRES_DATABASE" != 'postgres' ]; then
      createSql="CREATE DATABASE $POSTGRES_DATABASE OWNER $POSTGRES_USERNAME;"
      echo $createSql | gosu postgres postgres --single -jE
      echo
    fi

    # Starting a temporary server for setup
    gosu postgres pg_ctl -D "$PGDATA" \
        -o "-c listen_addresses=''" \
        -w start

    echo
	
	# If the file exists then execute it over the db created
	if [[ -a $SQL_FILE ]]; then
		gosu postgres psql \
		--username "$POSTGRES_USERNAME" \
		--dbname "$POSTGRES_DATABASE" \
		-v db="$POSTGRES_DATABASE"\
		-v user="$POSTGRES_USERNAME"\
		-f $SQL_FILE 
    fi

   	# Stopping the temporary server
    gosu postgres pg_ctl -D "$PGDATA" -m fast -w stop

    # Adding host and authMethod entry to pg_hba
    { echo; echo "host all all 0.0.0.0/0 $authMethod"; } >> "$PGDATA"/pg_hba.conf
fi

exec gosu postgres "$@"
