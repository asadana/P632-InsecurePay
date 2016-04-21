# Base image
FROM alpine

MAINTAINER  Amish <shah7@iu.edu>, Ankit <asadana@iu.edu>,  Jaini <voraj@iu.edu>, Janak <jbhalla@iu.edu>, Madhukiran <madhradh@iu.edu>
# Big thanks to kiasaki over at github

RUN echo "@edge http://nl.alpinelinux.org/alpine/edge/main" >> /etc/apk/repositories && \
	apk add --update curl ca-certificates bash && \
    apk update && apk add curl "postgresql@edge<9.6" "postgresql-contrib@edge<9.6" && \
    curl -o /usr/local/bin/gosu -sSL "https://github.com/tianon/gosu/releases/download/1.2/gosu-amd64" && \
    chmod +x /usr/local/bin/gosu && \
    apk del curl && \
    rm -rf /var/cache/apk/*
	
# Setting up environment variables
ENV LANG en_US.UTF8
ENV PGDATA /var/lib/postgresql/data
VOLUME /var/lib/postgresql/data

# These variables are used to create tables and setup the database.
# To link to the tomcat server, these values have been hardcoded in the war file.
# For any alterations, the InsecurePayServiceServer war would have to be recompiled
# with similar changes in InsecurePayServer/src/com/cigital/insecurepay/common/Constants.java
ENV POSTGRES_USERNAME insecurepay
ENV POSTGRES_PASSWORD insecurepassword
ENV POSTGRES_DATABASE insecurepaydatabase
ENV SQL_FILE /home/temp/dbsetup.sql

# Expose internal docker port
EXPOSE 5432

# Setting up a work dir
RUN mkdir -p /home/temp
WORKDIR /home/temp/

# Copy entrypoint script and dbsetup.sql
# docker-psql-entrypoint.sh contains the setup for the database
# dbsetup.sql contains the setup for the tables and populating values
COPY ./docker-psql-entrypoint.sh /home/temp
COPY ./dbsetup.sql /home/temp

# Making script executable
RUN chmod +x /home/temp/docker-psql-entrypoint.sh

# Setting up entrypoint to be executed when docker is run
ENTRYPOINT ["/home/temp/docker-psql-entrypoint.sh"]
CMD ["postgres"]
