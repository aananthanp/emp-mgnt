package com.tech.gov.gds.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tech.gov.gds.exception.EmployeeSalaryDeSerializerException;
import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

public class CustomEmployeeSalaryDeSerializer extends StdDeserializer<EmployeeSalaryDTO> {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final int SCALE_VALUE = 10;
    protected CustomEmployeeSalaryDeSerializer(Class<EmployeeSalaryDTO> t) {
        super(t);
    }

    public CustomEmployeeSalaryDeSerializer() {
        this(null);
    }

    @Override
    public EmployeeSalaryDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        Integer transactionId = null;
        JsonNode value = node.get("transactionId");
        if (value != null) {
            transactionId = Integer.valueOf(value.asText());
        }
        String name = node.get("name").asText();

        BigDecimal salary;
        try {
            salary = new BigDecimal(node.get("salary").asText().trim());
        } catch (NumberFormatException e) {
            throw new EmployeeSalaryDeSerializerException("Invalid salary data");
        } catch (Exception e) {
            throw new EmployeeSalaryDeSerializerException("Invalid salary data");
        }

        return EmployeeSalaryDTO.builder()
                .transactionId(transactionId)
                .name(name)
                .salary(salary.setScale(SCALE_VALUE, RoundingMode.HALF_UP))
                .build();

    }
}
