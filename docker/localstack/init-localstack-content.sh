#!/bin/bash
# This script will run inside docker container.

KINESIS_STREAM="customersStream addressesStream customerAddressesStream"
KINESIS_ENDPOINT="${KINESIS_ENDPOINT:-https://localhost:4568/}"
KINESIS_CONFIG="--cli-read-timeout=1 --endpoint-url=$KINESIS_ENDPOINT --no-verify-ssl"

while sleep 10; do
  aws ${KINESIS_CONFIG} kinesis list-streams && break;
done

for i in ${KINESIS_STREAM}; do
  aws ${KINESIS_CONFIG} kinesis create-stream --shard-count 1 --stream-name $i;
done
