package com.prosoft.serializer;

import com.prosoft.domain.Person;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * PersonSerializer используется для преобразования объектов Person в байтовый массив при отправке в Kafka.
 */
public class PersonSerializer implements Serializer<Person> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Person data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Person to JSON", e);
        }
    }

    @Override
    public void close() {
    }
}
