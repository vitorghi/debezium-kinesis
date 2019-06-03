# debezium-kinesis
Validating embedded debezium, streaming change data into Kinesis

It's expected to have:
- AWS CLI configured (must run `aws configure` with a default profile) 
- Java 8
- maven 3+

# Run with

> docker-compose up

Docker compose will start localstack and a mysql prepopulated database.

Then start debezium with:
> mvn clean install

> mvn exec:java

To verify if data is being streamed, use the bash script *get-records* to stream kinesis into console with: `./get-records.sh`
