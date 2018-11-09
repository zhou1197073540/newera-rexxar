package com.mzkj.news_classifier;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * create by zhouzhenyang on 2018/10/10
 */
public class TaggedNewsSink extends RichSinkFunction<TagEvent> {

    private static final String UPSERT_CASE =
            "insert into news_tagged (guid,title,tags,datetime)" +
                    "values (?,?,?,?) on conflict(guid) do nothing";
    private final Logger logger = LoggerFactory.getLogger(TaggedNewsSink.class);

    private PreparedStatement statement;
    private Connection connection;

    private Connection getConn() throws Exception {
        for (int i = 0; i < 3; i++) {
            try {
                Class.forName("org.postgresql.Driver");
                String username = "root";
                String passwd = "root123456";
                connection =
                        DriverManager.getConnection(
                                "jdbc:postgresql://172.16.20.205:5432/ow", username, passwd);
                return connection;
            } catch (Exception e) {
                logger.error("retry " + i + " " + this.getClass().getName(), e);
                Thread.sleep(500);
            }
        }
        throw new RuntimeException(this.getClass().getName() + " can not get connection from database");
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        try {
            if (connection.isClosed()) {
                connection = getConn();
            }
        } catch (Exception e) {
            connection = getConn();
        }
        statement = connection.prepareStatement(UPSERT_CASE);
    }

    @Override
    public void close() throws Exception {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            if (connection != null && connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {

        }
        super.close();
    }

    @Override
    public void invoke(TagEvent tagEvent, Context context) throws Exception {
        if (!tagEvent.getTags().isEmpty()) {
            Instant datetime;
            if (null == tagEvent.getDatetime()) {
                datetime = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8));
            } else {
                datetime = tagEvent.getDatetime().toInstant(ZoneOffset.ofHours(8));
            }
            try {
                statement.setString(1, tagEvent.getGuid());
                statement.setString(2, tagEvent.getTitle());
                statement.setArray(3,
                        connection.createArrayOf("VARCHAR", tagEvent.getTags().toArray()));
                statement.setTimestamp(4, Timestamp.from(datetime));
                statement.addBatch();
                statement.executeBatch();
            } catch (Exception e) {
                logger.error(TagEvent.class.getName() + " data error " + tagEvent.toString(), e);
            }
        }
    }

}
