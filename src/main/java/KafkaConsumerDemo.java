import com.sphonic.transactionaccumulator.avro.TransactionRecord;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerDemo {

    public static void main(String[] args){
        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("group.id", "txn-accumulator-consumer");
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

        KafkaConsumer<String,TransactionRecord> consumer = new KafkaConsumer<String, TransactionRecord>(properties);
        consumer.subscribe(Collections.singleton("local.wfm.transactions.mi"));

        while (true) {
            ConsumerRecords<String, TransactionRecord> consumerRecords = consumer.poll(100);
            for(ConsumerRecord<String, TransactionRecord> record: consumerRecords){
                System.out.println("TransactionRecord: " + record.value());
                System.out.println("Partition: " + record.partition() +
                        ", Offset: " + record.offset() +
                        ", Key: " + record.key() +
                        ", Value: " + record.value()
                );
            }
            consumer.commitSync();
        }
    }
}
