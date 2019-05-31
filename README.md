# debezium-kinesis
Validating embedded debezium streaming change data into Kinesis

# Run with

> docker-compose up

Docker compose will start localstack and a mysql prepopulated database.

Then start debezium with:
> mvn clean install

> mvn exec:java

When needed, use the bash script *get-records* to stream kinesis into console with: `./get-records.sh`
