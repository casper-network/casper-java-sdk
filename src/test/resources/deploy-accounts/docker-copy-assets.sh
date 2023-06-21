#!/usr/bin/env bash
BASEDIR=$(pwd)
# clear the assets folder
rm -rf  ${BASEDIR}/nctl
mkdir ${BASEDIR}/nctl
docker cp storm-nctl:/home/casper/casper-node/utils/nctl/assets/net-1/users ${BASEDIR}/nctl/
