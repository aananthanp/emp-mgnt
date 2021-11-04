package com.tech.gov.gds.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tech.gov.gds.exception.BigDecimalDeSerializerException;

import java.io.IOException;
import java.math.BigDecimal;

public class CustomBigDecimalDeSerializer extends StdDeserializer<BigDecimal> {


    protected CustomBigDecimalDeSerializer(Class<BigDecimal> t) {
        super(t);
    }

    public CustomBigDecimalDeSerializer() {
        this(null);
    }

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            return new BigDecimal(p.getText());
        } catch (Exception e) {
            throw new BigDecimalDeSerializerException(e.getMessage());
        }
    }
}
