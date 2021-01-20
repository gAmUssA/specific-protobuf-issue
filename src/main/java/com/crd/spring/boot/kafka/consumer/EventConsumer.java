package com.crd.spring.boot.kafka.consumer;

import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EventConsumer extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private KafkaConsumer<String, Object> consumer;

    public EventConsumer(String bootstrapServers, String schemaRegistryUrl,
                         String eventType1Topic, String eventType2Topic) {

        Properties props = new Properties();

        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", "EventTypeConsumer");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class);
        props.put(KafkaProtobufDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        //props.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE, ...);

        consumer = new KafkaConsumer<>(props);
        List<String> topicList = new ArrayList<>();
        topicList.add(eventType1Topic);
        topicList.add(eventType2Topic);
        consumer.subscribe(topicList);
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {

            ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, Object> record : records) {
                String topic = record.topic();

                // we should see these as DynamicMessage type if no specific pbuf type set

                String eventClassType = record.value().getClass().getName();
                logger.info("EventConsumer: topic:  " + topic + "; MsgType: " + eventClassType + "; EventConsumer:  offset = {}, key = {}, value = {}",
                        record.offset(), record.key(), record.value().toString());
            }
        }
    }
}