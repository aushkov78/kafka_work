package com.prosoft;

import com.prosoft.config.KafkaConfig;
import com.prosoft.domain.Person;
import com.prosoft.domain.Symbol;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Webinar-02: Kafka producer-service (отправка объектов класса Person)
 * Использования метода producer.send(producerRecord) с обработкой результата отправки через Callback.
 */
public class KafkaProducerApp {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerApp.class);
    private static final short MAX_MESSAGE = 26;

    public static void main(String[] args) {
        try (KafkaProducer<Long, Symbol> producer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {
            byte b = 65;
            for (byte i = b; i < b+25; i++) {
//                Person person = createPerson(i);
                char s = (char)i;
                Symbol smb = new Symbol(i+1, s, (i % 2)==0 ? "blue" : "green",i);

                String topic = KafkaConfig.TOPIC_CONSONANTS;
                if (s =='A' || s=='E' || s =='I' || s=='O' ||s=='U')
                    topic = KafkaConfig.TOPIC_VOWELS;
                long timestamp = System.currentTimeMillis();
                ProducerRecord<Long, Symbol> producerRecord = new ProducerRecord<>( topic, KafkaConfig.PARTITION,
                        timestamp, smb.getId(), smb);

                /**
                 * Анонимный внутренний класс (Callback), содержащий только один метод onCompletion(), можно записать
                 * через лямбду
                 */
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null) {
                            logger.error("Error sending message: {}", e.getMessage(), e);
                        } else {
                            logger.info("Sent record: key={}, value={}, partition={}, offset={}",
                                    smb.getId(), smb, recordMetadata.partition(), recordMetadata.offset());                        }
                    }
                });
                logger.info("Отправлено сообщение: key-{}, value-{}", smb.getId(), smb);
            }
            logger.info("Отправка завершена.");
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщений в Kafka", e);
        }
    } // todo показать без try -with-resources c вызовом .flush() .close() .close(Duration.ofSeconds(60))

    private static Person createPerson(int index) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
        return new Person(index, "FirstName-" + currentTime, "LastName" + index, 20 + index);
    }

}
