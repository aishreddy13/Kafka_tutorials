package io.condu.demos.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());

    public static void main(String[] args) throws InterruptedException {
        log.info("hello");

        //create producer properties
        Properties properties = new Properties();
        //connect to localhost
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");


        //set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // properties.setProperty("batch.size", "400");
        //create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for(int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                String topic = "demo_java";
                String key = "id" + i;
                String value = "hello world " + i;
                //create a producer record
                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<>(topic, key, value);

                //send data
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        //executes every time a record  successfully sent or an exception is thrown

                        if (e == null) {
                            log.info("Key" + key + "| Partition:" + recordMetadata.partition());
                        } else {
                            log.error("error while producing", e);
                        }
                    }
                });
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        // tell the producer to send all data and block until done --synchronous
        producer.flush();

        //flush and close the producer
        producer.close();
    }
}
