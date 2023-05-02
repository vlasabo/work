package com.doctrine7.config;

import com.doctrine7.model.SheduleChangeDto;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@NoArgsConstructor
public class GenericDeserializerJsonAndLong extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data)
    {
        switch (topic) {
            case "shedulechange", "sheduledelete", "filling", "startCommand" -> {
                try (JsonDeserializer<SheduleChangeDto> topicOneDeserializer = new JsonDeserializer<>()) {
                    topicOneDeserializer.addTrustedPackages("*");
                    return topicOneDeserializer.deserialize(topic, headers, data);
                }
            }
            case "blockedByUser" -> {
                try (LongDeserializer topicTwoDeserializer = new LongDeserializer()) {
                    return topicTwoDeserializer.deserialize(topic, headers, data);
                }
            }
        }
        return super.deserialize(topic, data);
    }
}
