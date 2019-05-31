#!/bin/bash
# This script will run inside docker container.

SQS_QUEUE="SomeQueue"
SQS_ENDPOINT="${SQS_ENDPOINT:-http://localhost:4576/}"
SQS_CONFIG="--cli-read-timeout=1 --endpoint-url=$SQS_ENDPOINT"

DYNAMO_ENDPOINT="${DYNAMO_ENDPOINT:-http://localhost:4569/}"
DYNAMO_CONFIG="--cli-read-timeout=1 --endpoint-url=$DYNAMO_ENDPOINT"

KINESIS_STREAM="SomeStream"
KINESIS_ENDPOINT="${KINESIS_ENDPOINT:-http://localhost:4568/}"
KINESIS_CONFIG="--cli-read-timeout=1 --endpoint-url=$KINESIS_ENDPOINT"

while sleep 10; do
  aws ${SQS_CONFIG} sqs list-queues && break;
done

for i in ${SQS_QUEUE}; do
  aws ${SQS_CONFIG} sqs create-queue --queue-name $i;
done

while sleep 10; do
  aws ${DYNAMO_CONFIG} dynamodb list-tables && break;
done

aws ${DYNAMO_CONFIG} dynamodb create-table --table-name Order \
  --attribute-definitions \
    AttributeName=Protocol,AttributeType=N \
    AttributeName=UserKey,AttributeType=S \
    AttributeName=CreatedAt,AttributeType=N \
    AttributeName=StatusAndCreation,AttributeType=S \
  --key-schema \
    AttributeName=UserKey,KeyType=HASH \
    AttributeName=Protocol,KeyType=RANGE \
  --local-secondary-indexes \
    'IndexName=StatusAndCreationIndex,KeySchema=[{AttributeName=UserKey,KeyType=HASH},{AttributeName=StatusAndCreation,KeyType=RANGE}],Projection={ProjectionType=KEYS_ONLY}' \
    'IndexName=CreationIndex,KeySchema=[{AttributeName=UserKey,KeyType=HASH},{AttributeName=CreatedAt,KeyType=RANGE}],Projection={ProjectionType=KEYS_ONLY}' \
  --provisioned-throughput \
    ReadCapacityUnits=1,WriteCapacityUnits=1

while sleep 10; do
  aws ${KINESIS_CONFIG} kinesis list-streams && break;
done

for i in ${KINESIS_STREAM}; do
  aws ${KINESIS_CONFIG} kinesis create-stream --shard-count 1 --stream-name $i;
done
