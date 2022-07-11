import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Properties;

public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);
    public static final String KAFKA_SERVER = "localhost:9092";
    private KafkaProducer<String, String> producer;
    private Admin admin;

    public Client(Properties properties){
        this.admin = Admin.create(properties);
        this.producer = new KafkaProducer<>(properties);
    }

    public void createTopic(String topicName){
        int partitions = 1;
        short replicationFactor = 1;
        NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
        CreateTopicsResult result = admin.createTopics(Collections.singleton(newTopic));
    }

    public void producer(String topicName, String message){
        int i = 0;
        while (i++ != 100){
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>(topicName, message + ' ' + i);
            producer.send(producerRecord);

            logger.info("Sent a message {} {} in time: {}", message, i, System.currentTimeMillis());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        producer.flush();
        producer.close();
    }
}




