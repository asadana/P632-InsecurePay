FROM alpine


RUN echo "@edge http://nl.alpinelinux.org/alpine/edge/main" >> /etc/apk/repositories && \
	apk add --update curl ca-certificates bash && \
    apk update && apk add curl "postgresql@edge<9.6" "postgresql-contrib@edge<9.6" && \
    mkdir docker-entrypoint-initdb.d && \
    curl -o /usr/local/bin/gosu -sSL "https://github.com/tianon/gosu/releases/download/1.2/gosu-amd64" && \
    chmod +x /usr/local/bin/gosu && \
    apk del curl && \
    rm -rf /var/cache/apk/*
	
ENV LANG en_US.UTF8
ENV PGDATA /var/lib/postgresql/data
VOLUME /var/lib/postgresql/data

ENV POSTGRES_USERNAME insecurepay
ENV POSTGRES_PASSWORD insecurepassword
ENV POSTGRES_DATABASE insecurepaydatabase
ENV SQL_FILE /home/temp/dbsetup.sql

#EXPOSE PortNo
EXPOSE 5432

RUN mkdir -p /home/temp
# Setting up a work dir
WORKDIR /home/temp/

# Copy and run example script
COPY ./docker-entrypoint.sh /home/temp
COPY ./dbsetup.sql /home/temp
# RUN chmod +x /home/temp/docker-entrypoint.sh
ENTRYPOINT ["/home/temp/docker-entrypoint.sh"]

CMD ["postgres"]


