#!/bin/sh
# Builds and starts the server on http://localhost:8080. Extra arguments are passed to Spring Boot, e.g.
#   ./server.sh --spring.profiles.active=mysql
#   ./server.sh --server.port=8081
# Use --no-build to skip the build.
set -e
cd "$(dirname "$0")/java-server"
if [ -x /usr/libexec/java_home ]; then
  JAVA_HOME="$(/usr/libexec/java_home -v 21)"
  export JAVA_HOME
fi
if [ "$1" = "--no-build" ]; then
  shift
else
  ./mvnw -q -B -DskipTests install
fi
exec "${JAVA_HOME:+$JAVA_HOME/bin/}java" -jar measurementkit/measurementkit-application/target/measurementkit-application-0.1.0-SNAPSHOT.jar "$@"
