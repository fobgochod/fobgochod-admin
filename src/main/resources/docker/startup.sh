#!/bin/sh
sed -i "s#{profile_active}#${profile_active:-prod}#g" /usr/local/fobgochod/application.yml
sed -i "s#{mongodb_uri}#${mongodb_uri:-}#g" /usr/local/fobgochod/application.yml

# set java opts parameters
JAVA_OPTS="${java_opts}"
if [ -z "$JAVA_OPTS" ]; then
  JAVA_OPTS="-Xms500m -Xmx500m"
fi
# set dmc jar version
JAR_NAME="fobgochod-admin-1.0.0.jar"
# run command
java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar $JAR_NAME
