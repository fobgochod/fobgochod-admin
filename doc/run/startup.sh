#!/bin/bash
docker rm -f fobgochod-admin
docker run -idt --restart=always --privileged=true --name fobgochod-admin \
  -p 7003:8080 \
  -e java_opts='-Xms128m -Xmx300m' \
  -e profile_active='prod' \
  -e mongodb_uri='mongodb://root:root@172.19.215.14:27017/admin' \
  -e base_uri='http://fobgochod.com' \
  fobgochod/fobgochod-admin:1.0.0