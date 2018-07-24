import com.sphonic.transactionaccumulator.avro.TransactionRecord;
import com.sphonic.transactionaccumulator.avro.workflowStruct;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class KafkaProducer {

    public static void main(String[] args){
        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "3");
        properties.setProperty("linger.ms", "1");
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

        TransactionRecord record = TransactionRecord.newBuilder()
                .setTransactionUuid("txUUID")
                .setRequestId("requestId")
                .setRequestTimestamp("some req timestamp")
                .setResponseTimestamp("some rep timestamp")
                .setDuration(20)
                .setTimeoutDuration(0)
                .setSuccessType("DONE")
                .setWorkflow(workflowStruct.newBuilder().setAuthor("NONE").setHash("NONE").setLastUpdated("NONE").setName("TEST").setUuid("UUID").setVersion(0).build())
                .setVendorCalls(new ArrayList())
                .setTrace(new ArrayList())
                .setMerchantSpecific(new ArrayList())
                .build();


        Producer<String, TransactionRecord> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, TransactionRecord>(properties);
        ProducerRecord<String, TransactionRecord> producerRecord = new ProducerRecord<String, TransactionRecord>("local.wfm.transactions.mi", "4", record);

        producer.send(producerRecord);
        producer.close();
    }
}
