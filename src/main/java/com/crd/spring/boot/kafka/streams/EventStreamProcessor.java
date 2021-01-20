package com.crd.spring.boot.kafka.streams;

import com.crd.events.EventType1Messages;
import com.crd.events.EventType2Messages;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
@Configuration
@EnableBinding(EventStreamProcessor.StreamProcessor.class)
public class EventStreamProcessor {

    private final Logger logger = LoggerFactory.getLogger(EventStreamProcessor.class);

    public EventStreamProcessor() {
        logger.info("ctor");
    }

    @StreamListener
    public void consume(
            @Input("eventtype1") KStream<String, EventType1Messages.EventType1> eventType1Stream,
            @Input("eventtype2") KStream<String, EventType2Messages.EventType2> eventType2Stream)
            throws IOException {

        logger.info("consume: enter");

        // we should be able to process the eventType1Stream as EventType1 instances.
        // They will be DynamicMessages if not configured correctly

        eventType1Stream.foreach(new ForeachAction<String, EventType1Messages.EventType1>() {
            public void apply(String key, EventType1Messages.EventType1 event) {
                logger.info("***** eventType1Stream: " + key + ": " + event.toString());
            }
        });

        // we should be able to process the eventType2Stream as EventType2 instances.
        // They will be DynamicMessages if not configured correctly

        eventType2Stream.foreach(new ForeachAction<String, EventType2Messages.EventType2>() {
            public void apply(String key, EventType2Messages.EventType2 event) {
                logger.info("***** eventType2Stream: " + key + ": " + event.toString());
            }
        });
    }

    interface StreamProcessor {
        @Input("eventtype1")
        KStream inputStreamType1();

        @Input("eventtype2")
        KStream inputStreamType2();
    }
}
