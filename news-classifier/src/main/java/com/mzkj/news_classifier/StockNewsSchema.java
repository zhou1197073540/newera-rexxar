package com.mzkj.news_classifier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

/**
 * create by zhouzhenyang on 2018/9/20
 */
public class StockNewsSchema implements
        DeserializationSchema<StockNewsEvent>,
        SerializationSchema<TagEvent> {
    private static final long serialVersionUID = -4116648147166226485L;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public StockNewsEvent deserialize(byte[] bytes) throws IOException {
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        return StockNewsEvent.fromBytes(bytes, mapper);
    }

    @Override
    public boolean isEndOfStream(StockNewsEvent stockNewsEvent) {
        return false;
    }

    @Override
    public byte[] serialize(TagEvent tagEvent) {
        return TagEvent.toBytes(tagEvent, mapper);
    }

    @Override
    public TypeInformation<StockNewsEvent> getProducedType() {
        return TypeInformation.of(StockNewsEvent.class);
    }
}
