# Base: Alpine
FROM alpine

MAINTAINER  Amish <shah7@iu.edu>, Ankit <asadana@iu.edu>,  Jaini <voraj@iu.edu>, Janak <jbhalla@iu.edu>, Madhukiran <madhradh@iu.edu>

# Run any commands
RUN apk upgrade --update && \
    apk add --update curl ca-certificates bash

# Expose port
EXPOSE 8080
EXPOSE 5432

# Setting up a work dir
WORKDIR /root/temp

# Copy and run example script
COPY ./postInstall.sh /root/temp
RUN chmod +x /root/temp/postInstall.sh
RUN /bin/bash /root/temp/postInstall.sh
