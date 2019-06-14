package io.debezium.examples.kinesis;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import io.debezium.config.Configuration;
import io.debezium.connector.mysql.MySqlConnectorConfig;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.relational.history.MemoryDatabaseHistory;
import io.debezium.util.Clock;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Demo for using the Debezium Embedded API to send change events to Amazon Kinesis.
 * <p>
 * Using synchronous kinesis API. For production it's needed to use KPL.
 */
public class ChangeDataSender implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeDataSender.class);

    private static final String APP_NAME = "debeziumKinesisConnector";
    private static final String KINESIS_REGION_CONF_NAME = "kinesis.region";
    private static final String KINESIS_STREAM_CONF_NAME = "kinesis.stream";

    private final Configuration config;
    private final JsonConverter valueConverter;
    private final AmazonKinesis kinesisClient;

    private ChangeDataSender() {
        this.config = Configuration.empty().withSystemProperties(Function.identity()).edit()
                .with(EmbeddedEngine.CONNECTOR_CLASS, "io.debezium.connector.mysql.MySqlConnector")
                .with(EmbeddedEngine.ENGINE_NAME, APP_NAME)
                .with(MySqlConnectorConfig.SERVER_NAME, APP_NAME)
                .with(MySqlConnectorConfig.SERVER_ID, 8192)

                // for demo purposes let's store offsets and history only in memory
                .with(EmbeddedEngine.OFFSET_STORAGE, "org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
                .with(MySqlConnectorConfig.DATABASE_HISTORY, MemoryDatabaseHistory.class.getName())

                // Send JSON without schema
                .with("schemas.enable", false)
                .build();

        this.valueConverter = new JsonConverter();
        this.valueConverter.configure(config.asMap(), false);

        final String regionName = config.getString(KINESIS_REGION_CONF_NAME);

        final AWSCredentialsProvider credentialsProvider = new ProfileCredentialsProvider("default");

        AmazonKinesisClientBuilder kinesisClientBuilder = AmazonKinesisClientBuilder.standard()
                .withCredentials(credentialsProvider);

        if (System.getProperty("testEnvironment").equals("true")) {
            LOGGER.warn("Running in local environment.");
            AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder
                    .EndpointConfiguration("http://localhost:4568", regionName);

            kinesisClientBuilder.withEndpointConfiguration(endpoint);
            System.setProperty("com.amazonaws.sdk.disableCbor", "true");
        }

        this.kinesisClient = kinesisClientBuilder.build();
    }

    @Override
    public void run() {
        final EmbeddedEngine engine = EmbeddedEngine.create()
                .using(config)
                .using(this.getClass().getClassLoader())
                .using(Clock.SYSTEM)
                .notifying(this::sendRecord)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Requesting embedded engine to shut down");
            engine.stop();
        }));

        awaitTermination(executor);
    }

    private void awaitTermination(ExecutorService executor) {
        try {
            while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                LOGGER.info("Waiting another 10 seconds for the embedded engine to shut down");
            }
        } catch (InterruptedException e) {
            LOGGER.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
        }
    }

    private void sendRecord(SourceRecord record) {
        // We are interested only in data events not schema change events
        if (record.topic().equals(APP_NAME)) {
            return;
        }

        if (record.keySchema() == null) {
            LOGGER.error("The keySchema is missing. Something is wrong.");
            return;
        }

        SchemaBuilder schemaBuilder = SchemaBuilder.struct()
                .field("key", record.keySchema());

        // For deletes, the value node is null
        if (record.valueSchema() != null) {
            schemaBuilder.field("value", record.valueSchema());
        }

        Schema schema = schemaBuilder.build();
        Struct message = new Struct(schema);
        message.put("key", record.key());

        if (record.value() != null) {
            message.put("value", record.value());
        }

        String partitionKey = String.valueOf(record.key() != null ? record.key().hashCode() : -1);
        final byte[] payload = valueConverter.fromConnectData("KafkaTopic", schema, message);

        PutRecordRequest putRecord = new PutRecordRequest();
        putRecord.setStreamName(config.getString(KINESIS_STREAM_CONF_NAME));
        putRecord.setPartitionKey(partitionKey);
        putRecord.setData(ByteBuffer.wrap(payload));

        PutRecordResult putRecordResult = kinesisClient.putRecord(putRecord);
        LOGGER.info("Result: " + putRecordResult.toString());
    }

    public static void main(String... args) {
        new ChangeDataSender().run();
    }
}
