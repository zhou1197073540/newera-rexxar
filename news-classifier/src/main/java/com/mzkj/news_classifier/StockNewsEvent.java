package com.mzkj.news_classifier;

import akka.actor.ProviderSelection;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

/**
 * create by zhouzhenyang on 2018/9/20
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockNewsEvent {

    private String guid;
    private String title;
    private String content;
    private String whole_content;
    private Set<String> tags = new HashSet<>(10);
    @JsonAlias("publish_time")
    private LocalDateTime datetime;
    private long timestamp = 0;
    private static final Logger log = LoggerFactory.getLogger(StockNewsEvent.class);

    public StockNewsEvent() {

    }

    public StockNewsEvent(String guid) {
        this.guid = guid;
    }

    public StockNewsEvent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static StockNewsEvent fromBytes(byte[] msg, ObjectMapper mapper) {
        try {
            final StockNewsEvent event = mapper.readerFor(StockNewsEvent.class).readValue(msg);
            if (event.getTimestamp() == 0) {
                event.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
            }
            return event;
        } catch (Exception e) {
            log.error("wrong!", e);
            return null;
        }
    }

    public static byte[] toBytes(StockNewsEvent stockNewsEvent, ObjectMapper mapper) {
        try {
            return mapper.writerFor(StockNewsEvent.class).writeValueAsBytes(stockNewsEvent);
        } catch (JsonProcessingException e) {
            log.error("wrong!", e);
            return null;
        }
    }


    public void addTag(String tag) {
        tags.add(tag);
    }

}
