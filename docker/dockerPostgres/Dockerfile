# Base: Alpine
FROM alpine

MAINTAINER  Amish <shah7@iu.edu>, Ankit <asadana@iu.edu>,  Jaini <voraj@iu.edu>, Janak <jbhalla@iu.edu>, Madhukiran <madhradh@iu.edu>


# Run any commands
RUN apk upgrade --update && \
    apk add --update curl ca-certificates bash && \
	apk update && apk add curl "postgresql@edge<9.6" "postgresql-contrib@edge<9.6" 

# Expose port
EXPOSE 5432

ENV LANG en_US.utf8
ENV PGDATA /var/lib/postgresql/data
VOLUME /var/lib/postgresql/data

# Setting up a work dir
WORKDIR /root/temp

# Copy and run example script
COPY ./postInstall.sh /root/temp
RUN chmod +x /root/temp/postInstall.sh
RUN /bin/bash /root/temp/postInstall.sh

#ERROR: 'postgresql-client-9.5 9.5.2-1.pgdg80+1' is not a valid dependency, format is name(@tag)([<>=]version)
#9.5.2-1.pgdg80+1" && \