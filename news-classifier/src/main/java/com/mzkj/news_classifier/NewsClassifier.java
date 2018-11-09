package com.mzkj.news_classifier;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.types.Row;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * create by zhouzhenyang on 2018/9/20
 */
public class NewsClassifier {

    public void run() throws Exception {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ParameterTool parameterTool;
        try (InputStream resourceStream = loader.getResourceAsStream("config.properties")) {
            parameterTool = ParameterTool.fromPropertiesFile(resourceStream);
        }
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
        env.enableCheckpointing(5000); // create a checkpoint every 5 seconds
        env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<TagEvent> input = env
                .addSource(
                        new FlinkKafkaConsumer011<>(
                                parameterTool.getRequired("input-topic"),
                                new StockNewsSchema(),
                                parameterTool.getProperties())
                                .assignTimestampsAndWatermarks(new CustomWatermarkExtractor()))
                .keyBy("guid")
                .map(new StockTagPinner());

        input.addSink(new TaggedNewsSink());
        input.addSink(new FlinkKafkaProducer011<>(
                parameterTool.getRequired("output-topic"),
                new StockNewsSchema(),
                parameterTool.getProperties()));
        env.execute("stock-tagger");
    }

    private static class CustomWatermarkExtractor implements AssignerWithPeriodicWatermarks<StockNewsEvent> {

        private static final long serialVersionUID = -742759155861320823L;

        private long currentTimestamp = Long.MIN_VALUE;

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentTimestamp == Long.MIN_VALUE ? Long.MIN_VALUE : currentTimestamp - 1);
        }

        @Override
        public long extractTimestamp(StockNewsEvent element, long previousElementTimestamp) {
            if (element.getTimestamp() == 0) {
                if (null != element.getDatetime()) {
                    this.currentTimestamp = element.getDatetime().toEpochSecond(ZoneOffset.ofHours(8));
                } else {
                    this.currentTimestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
                }
            } else {
                this.currentTimestamp = element.getTimestamp();
            }
            return this.currentTimestamp;
        }
    }
}

