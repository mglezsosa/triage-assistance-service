#!/usr/bin/env bash

set -e
cd `dirname $0`
cd applications
../gradlew build
cd ../docker
docker-compose build
docker-compose up --force-recreate
