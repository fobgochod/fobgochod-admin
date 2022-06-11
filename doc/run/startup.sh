#!/bin/bash
docker rm -f fobgochod-admin
docker run -idt --restart=always --privileged=true --name fobgochod-admin \
  -p 7003:7070 \
  -e java_opts='-Xms500m -Xmx500m' \
  -e profile_active='prod' \
  -e mongodb_uri='mongodb://172.19.215.14:27017/admin' \
  -e base_uri='http://admin.zhouxiao.co' \
  fobgochod/fobgochod-admin:1.0.0
