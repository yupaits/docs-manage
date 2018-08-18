package com.yupaits.docs.common.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 解决Long类型的主键转json传回前端丢失精度的问题，将Long转成String
 * @author yupaits
 * @date 2017/12/18
 */
public class LongJsonSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (value != null) {
            jsonGenerator.writeString(String.valueOf(value));
        }
    }
}
