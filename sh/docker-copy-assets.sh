#!/usr/bin/env bash
BASEDIR=$(builtin cd ..; pwd)
# clear the assets folder
rm -rf  ${BASEDIR}/src/test/resources/assets
mkdir ${BASEDIR}/src/test/resources/assets
docker cp cspr-nctl:/home/casper/casper-node/utils/nctl/assets/net-1/users ${BASEDIR}/src/test/resources/assets
