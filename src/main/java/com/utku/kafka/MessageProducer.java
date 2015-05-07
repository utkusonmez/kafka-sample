package com.utku.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Properties;

public class MessageProducer {

    private static Properties props = new Properties();
    private static Producer<String, String> producer;

    private static final String TOPIC = "kafkatopic";

    static {
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, String>(config);
    }

    public static void main(String[] args) {
        String message = RandomStringUtils.randomAlphabetic(10);
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, message);
        producer.send(data);
        producer.close();
        System.out.println("Message sent : " + message);
    }

}
