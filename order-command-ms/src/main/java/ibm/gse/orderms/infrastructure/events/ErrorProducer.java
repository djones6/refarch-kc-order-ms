package ibm.gse.orderms.infrastructure.events;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import ibm.gse.orderms.infrastructure.kafka.ApplicationConfig;


public class ErrorProducer implements EventEmitter {

    private static final Logger logger = LoggerFactory.getLogger(ErrorProducer.class);

    private KafkaProducer<String, String> kafkaProducer;

    public ErrorProducer() {
        Properties properties = ApplicationConfig.getProducerProperties("error-query-producer");
        kafkaProducer = new KafkaProducer<String, String>(properties);
    }

    @Override
    public void emit(Event event) throws InterruptedException, ExecutionException, TimeoutException {
        ErrorEvent errorEvent = (ErrorEvent) event;
        String value = new Gson().toJson(errorEvent);

        ProducerRecord<String, String> record = new ProducerRecord<>(ApplicationConfig.ERROR_TOPIC, value);

        Future<RecordMetadata> send = kafkaProducer.send(record);
        send.get(ApplicationConfig.PRODUCER_TIMEOUT_SECS, TimeUnit.SECONDS);
    }

    @Override
    public void safeClose() {
        try {
            kafkaProducer.close(ApplicationConfig.PRODUCER_CLOSE_TIMEOUT_SEC, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.warn("Failed to close Producer", e);
        }
    }

}
