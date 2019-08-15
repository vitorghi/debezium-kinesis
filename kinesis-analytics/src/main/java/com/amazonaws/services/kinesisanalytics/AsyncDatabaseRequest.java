package com.amazonaws.services.kinesisanalytics;

import com.amazonaws.services.kinesisanalytics.models.Product;
import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AsyncDatabaseRequest extends RichAsyncFunction<Integer, Product> {

    /**
     * The database specific client that can issue concurrent requests with callbacks
     */
    private transient Connection connection;

    @Override
    public void open(Configuration parameters) {
        connection = MySQLConnectionBuilder.createConnectionPool(
                "jdbc:mysql://localhost:3306/inventory?user=root&password=debezium");
    }

    @Override
    public void close() {
        connection.disconnect();
    }

    @Override
    public void asyncInvoke(Integer productId, ResultFuture<Product> productResultFuture) {
        CompletableFuture<QueryResult> result = connection.sendPreparedStatement("SELECT id, name, description, weight FROM products WHERE id = ?", Collections.singletonList(productId));

        result.thenAccept(dbResult -> {
            Optional<Product> product = dbResult
                    .getRows()
                    .stream()
                    .findFirst()
                    .map(r -> new Product(r.getInt(0), r.getString(1), r.getString(2), r.getFloat(3)));

            productResultFuture.complete(Collections.singleton(product.orElseThrow(RuntimeException::new)));
        });
    }
}
