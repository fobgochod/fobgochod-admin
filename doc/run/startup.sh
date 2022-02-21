#!/bin/bash
docker rm -f fobgochod-admin
docker run -idt --restart=always --privileged=true --name fobgochod-admin \
  -p 7003:8080 \
  -e java_opts='-Xms128m -Xmx300m' \
  -e profile_active='dev' \
  -e mongodb_uri='mongodb://172.19.215.14:27017/admin' \
  -e base_uri='http://zhouxiao.co' \
  fobgochod/fobgochod-admin:1.0.0
