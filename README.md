# debezium-kinesis
Validating embedded debezium, streaming change data into Kinesis

It's expected to have:
- AWS CLI configured (must run `aws configure` with a default profile) 
- Java 8
- maven 3+
- jq (CLI utils for json)

# Run with

> docker-compose up

Docker compose will start localstack and a mysql prepopulated database.

Then start debezium(inside debezium-kinesis folder) locally with:
> mvn clean install

> mvn exec:java

To verify if data is being streamed, use the bash script *get-records* to stream kinesis into console with: `./get-records.sh`. The script accepts the stream name as parameter, if argument is not provided, it runs with the stream provided in the custom localstack image.

To run Kinesis analytics locally:

Just run the main method inside the BasicStreamingJob java class.
