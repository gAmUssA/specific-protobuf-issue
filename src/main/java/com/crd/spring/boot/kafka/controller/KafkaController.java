package com.crd.spring.boot.kafka.controller;

import com.crd.events.EventType1Messages;
import com.crd.events.EventType2Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crd.spring.boot.kafka.producer.EventProducer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController implements ApplicationContextAware {

    private ApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    private final EventProducer eventProducer;


    @Autowired
    public KafkaController(EventProducer producer) {

        this.eventProducer = producer;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;
    }

    @PostMapping(value = "/publishType1")
    public void publishBusinessEventType1(@RequestParam(name="key") String key,
                                          @RequestParam(name="field1") String field1) {


        EventType1Messages.EventType1 eventType1 = EventType1Messages.EventType1.newBuilder().
                setField1(field1).build();
        this.eventProducer.send(key, eventType1);
    }
    @PostMapping(value = "/publishType2")
    public void publishBusinessEventType2(@RequestParam(name="key") String key,
                                          @RequestParam(name="field1") String field1,
                                          @RequestParam(name="field2") String field2) {


        EventType2Messages.EventType2 eventType2 = EventType2Messages.EventType2.newBuilder()
                .setField1(field1)
                .setField2(field2)
                .build();
        this.eventProducer.send(key, eventType2);
    }
}
