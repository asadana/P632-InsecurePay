#!/bin/bash
chown -R postgres "$PGDATA"

if [ -z "$(ls -A "$PGDATA")" ]; then
    gosu postgres initdb
    sed -ri "s/^#(listen_addresses\s*=\s*)\S+/\1'*'/" "$PGDATA"/postgresql.conf

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
	
    if [ "$POSTGRES_USERNAME" != 'postgres' ]; then
      op=CREATE
    else
      op=ALTER
    fi

    userSql="$op USER $POSTGRES_USERNAME WITH SUPERUSER $pass;"
    echo "==== Creating user $op"
    echo $userSql | gosu postgres postgres --single -jE
    echo


    if [ "$POSTGRES_DATABASE" != 'postgres' ]; then
      createSql="CREATE DATABASE $POSTGRES_DATABASE OWNER $POSTGRES_USERNAME;"
      echo "==== Creating db $POSTGRES_DATABASE"
      echo $createSql | gosu postgres postgres --single -jE
      echo
    fi

    # internal start of server in order to allow set-up using psql-client
    # does not listen on TCP/IP and waits until start finishes
    gosu postgres pg_ctl -D "$PGDATA" \
        -o "-c listen_addresses=''" \
        -w start

    echo
    
	echo "============"
	pwd
	echo "==== value of input variable $SQL_FILE"
	
	if [[ -a $SQL_FILE ]]; then
		gosu postgres psql --username "$POSTGRES_USERNAME" --dbname "$POSTGRES_DATABASE" -f $SQL_FILE 
    fi

	echo "=============="
	gosu postgres psql --list
	
    gosu postgres pg_ctl -D "$PGDATA" -m fast -w stop

    { echo; echo "host all all 0.0.0.0/0 $authMethod"; } >> "$PGDATA"/pg_hba.conf
fi

exec gosu postgres "$@"
# gosu postgres pg_ctl -D "$PGDATA" start
