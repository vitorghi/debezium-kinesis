# debezium-kinesis
Validating embedded debezium, streaming change data into Kinesis

It's expected to have:
- AWS CLI configured (must run `aws configure` with a default profile)
- awscli-local installed, `pip install awscli-local`
- Java 8
- maven 3+
- jq (CLI utils for json, used in the bash script to print stream records)

# Run with

> docker-compose up

Docker compose will start localstack and a mysql prepopulated database.

Then start debezium(inside debezium-kinesis folder) locally with:
> mvn clean install

> mvn exec:java

To verify if data is being streamed, use the bash script *get-records* to stream kinesis into console with: `./get-records.sh`. The script accepts the stream name as parameter, if argument is not provided, it runs with the stream provided in the custom localstack image.

To run Flink/Kinesis analytics locally:

First, some steps are needed to use kinesis connector, follow this link: https://ci.apache.org/projects/flink/flink-docs-stable/dev/connectors/kinesis.html

Then just run the main method inside the BasicStreamingJob java class.
