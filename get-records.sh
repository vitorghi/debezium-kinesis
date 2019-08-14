streamname="customerAddressesStream"
if [ -n "$1" ]
  then
    streamname=$1
fi
echo Retrieving records for $streamname...
iteratorType="LATEST"
config="--endpoint-url=https://localhost:4568 --no-verify-ssl"

shard=$(awslocal $config kinesis describe-stream --stream-name $streamname --query 'StreamDescription.Shards[0].ShardId' --output text)
iterator=$(awslocal $config kinesis get-shard-iterator --stream-name $streamname --shard-id $shard --shard-iterator-type $iteratorType --output text)
while output=$(awslocal $config kinesis get-records --shard-iterator $iterator); do
  iterator=$(echo $output | jq -r '.NextShardIterator')
  records=$(echo $output | jq -r '.Records[].Data')
  for record in $records; do
    echo "----START RECORD----"
    echo $(echo $record | base64 -d)
    echo "----END RECORD----"
  done
done
