package com.doctrine7.tgbot.config;

import com.doctrine7.model.bot.InputMessageDto;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;


@NoArgsConstructor
public class GenericSerializerJsonAndLong extends JsonSerializer<Object> {

    @Override
    public byte[] serialize(String topic, Headers headers, Object data) {
        switch (topic) {
            case "blockedByUser" -> {
                try (LongSerializer blockedByUserSerializer = new LongSerializer()) {
                    return blockedByUserSerializer.serialize(topic, headers, (long) data);
                }
            }
            case "inputCommand" -> {
                try (JsonSerializer<InputMessageDto> commandSerializer = new JsonSerializer<>()) {
                    return commandSerializer.serialize(topic, headers, (InputMessageDto) data);
                }
            }
            default -> {
                    return super.serialize(topic, headers, data);
            }
        }
    }

}
