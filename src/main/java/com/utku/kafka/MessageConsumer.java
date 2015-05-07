package com.utku.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.*;
import java.util.function.Function;

public class MessageConsumer {

    private static final String topic = "kafkatopic";

    private ConsumerConnector consumer;

    public MessageConsumer() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "testgroup");
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
    }

    public void consume(Function<KafkaStream<byte[], byte[]>, ConsumerIterator<byte[], byte[]>> f) {
        Map<String, Integer> topicCount = new HashMap<>();
        // Define single thread for topic
        topicCount.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);

        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);

        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> consumerIterator = f.apply(stream);
            while (consumerIterator.hasNext()) {
                System.out.println("Message from Single Topic :: " +
                        new String(consumerIterator.next().message()));
            }
        }

        if (consumer != null) {
            consumer.shutdown();
        }
    }

    public static void main(String[] args) {

//        new messages stream
        new MessageConsumer().consume((KafkaStream<byte[], byte[]> s) -> (s.iterator()));

//        sliding stream
//        new MessageConsumer().consume((KafkaStream<byte[], byte[]> s) -> (s.f(40)));


    }

}
