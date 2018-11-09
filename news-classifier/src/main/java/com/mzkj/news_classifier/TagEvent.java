package com.mzkj.news_classifier;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * create by zhouzhenyang on 2018/9/20
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagEvent {

    private String guid;
    private String title;
    private Set<String> tags;
    @JsonAlias("publish_time")
    private LocalDateTime datetime;
    private long timestamp = 0;
    private static final Logger log = LoggerFactory.getLogger(TagEvent.class);

    public TagEvent() {

    }

    public TagEvent(String guid, LocalDateTime datetime,long timestamp, Set<String> tags) {
        this.guid = guid;
        this.datetime = datetime;
        this.timestamp = timestamp;
        this.tags = new HashSet<>(10);
        this.tags.addAll(tags);
    }

    public static byte[] toBytes(TagEvent tagEvent, ObjectMapper mapper) {
        try {
            System.out.println(tagEvent);
            return mapper.writerFor(TagEvent.class).writeValueAsBytes(tagEvent);
        } catch (JsonProcessingException e) {
            log.error("wrong!", e);
            return null;
        }
    }
}
