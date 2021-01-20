package com.crd.spring.boot.kafka;

import com.crd.spring.boot.kafka.consumer.EventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crd.spring.boot.kafka.producer.EventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value="com.crd.spring.boot")
public class BusinessServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(BusinessServiceApplication.class);

	@Value("${spring.kafka.properties.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.properties.schema.registry.url}")
	private String schemaRegistryUrl;

	@Value("${spring.cloud.stream.bindings.eventtype1.destination}")
	private String eventType1Topic;

	@Value("${spring.cloud.stream.bindings.eventtype2.destination}")
	private String eventType2Topic;

	public static void main(String[] args) {
		SpringApplication.run(BusinessServiceApplication.class, args);
	}

	@Bean
	public EventProducer eventProducer() {

		EventProducer eventProducer =
				new EventProducer(bootstrapServers, schemaRegistryUrl, eventType1Topic, eventType2Topic);
		return eventProducer;
	}

	@Bean
	EventConsumer eventConsumer() {

		EventConsumer eventConsumer = new EventConsumer(bootstrapServers, schemaRegistryUrl, eventType1Topic, eventType2Topic);
		eventConsumer.start();
		return eventConsumer;
	}

}
