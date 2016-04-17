# Base: Alpine
FROM alpine

MAINTAINER  Amish <shah7@iu.edu>, Ankit <asadana@iu.edu>,  Jaini <voraj@iu.edu>, Janak <jbhalla@iu.edu>, Madhukiran <madhradh@iu.edu>
# Big thanks to jeanblanchard over at github

# GLIBC Version
ENV GLIBC_VERSION 2.23-r1
# Java Version
ENV JAVA_VERSION_MAJOR 8
ENV JAVA_VERSION_MINOR 77
ENV JAVA_VERSION_BUILD 03
ENV JAVA_PACKAGE       jre
ENV JAVA_SHA256_SUM    edd9db13e81913d8758263eb5e923cbe854d24c1d47a61a7d62064a7dba5255f
# Tomcat Version
ENV TOMCAT_VERSION_MAJOR 8
ENV TOMCAT_VERSION_FULL  8.0.33

# Download and install glibc
RUN apk add --update curl && \
  curl -o glibc.apk -L "https://github.com/andyshinn/alpine-pkg-glibc/releases/download/${GLIBC_VERSION}/glibc-${GLIBC_VERSION}.apk" && \
  apk add --allow-untrusted glibc.apk && \
  curl -o glibc-bin.apk -L "https://github.com/andyshinn/alpine-pkg-glibc/releases/download/${GLIBC_VERSION}/glibc-bin-${GLIBC_VERSION}.apk" && \
  apk add --allow-untrusted glibc-bin.apk && \
  /usr/glibc-compat/sbin/ldconfig /lib /usr/glibc/usr/lib && \
  echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf && \
  rm -f glibc.apk glibc-bin.apk

# Download and install oracle jre
RUN mkdir -p /opt && \
  	curl -jkLH "Cookie: oraclelicense=accept-securebackup-cookie" -o java.tar.gz \
    http://download.oracle.com/otn-pub/java/jdk/${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-b${JAVA_VERSION_BUILD}/${JAVA_PACKAGE}-${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-linux-x64.tar.gz && \
  	echo "$JAVA_SHA256_SUM  java.tar.gz" | sha256sum -c - && \
  	gunzip -c java.tar.gz | tar -xf - -C /opt && rm -f java.tar.gz && \
  	ln -s /opt/jre1.${JAVA_VERSION_MAJOR}.0_${JAVA_VERSION_MINOR} /opt/jre && \
  	rm -rf /opt/jre/lib/plugin.jar \
			/opt/jre/lib/ext/jfxrt.jar \
			/opt/jre/bin/javaws \
			/opt/jre/lib/javaws.jar \
			/opt/jre/lib/desktop \
			/opt/jre/plugin \
			/opt/jre/lib/deploy* \
			/opt/jre/lib/*javafx* \
			/opt/jre/lib/*jfx* \
			/opt/jre/lib/amd64/libdecora_sse.so \
			/opt/jre/lib/amd64/libprism_*.so \
			/opt/jre/lib/amd64/libfxplugins.so \
			/opt/jre/lib/amd64/libglass.so \
			/opt/jre/lib/amd64/libgstreamer-lite.so \
			/opt/jre/lib/amd64/libjavafx*.so \
			/opt/jre/lib/amd64/libjfx*.so

# Set environment
ENV JAVA_HOME /opt/jre
ENV PATH ${PATH}:${JAVA_HOME}/bin

# Expose port
EXPOSE 8080
