package com.mzkj.news.controller;

import com.mzkj.news.bean.News;
import com.mzkj.news.service.MsgService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class KafkaController {
    public static Logger logger = LoggerFactory.getLogger(KafkaController.class);
    @Autowired
    MsgService msgService;

    @Autowired
    KafkaTemplate<String, String> template;

    @RequestMapping("/send")
    @ResponseBody
    String send(String topic, String key, String data) {
        template.send(topic, key, data);
        return "success";
    }

    @KafkaListener(id = "test", topics = "tt")
    public void listenT1(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("test={} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

    @KafkaListener(id = "t2", topics = "output-tagged-news")
    public void listenPushNews(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("kafka收到新闻推送的消息，topic:{}，key:{},valye:{}", cr.topic(), cr.key(), cr.value());
//        msgService.pushNews(cr);
    }
}
