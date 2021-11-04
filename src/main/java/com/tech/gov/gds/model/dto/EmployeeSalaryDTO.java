package com.tech.gov.gds.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.opencsv.bean.CsvBindByPosition;
import com.tech.gov.gds.persistence.entity.EmployeeSalary;
import com.tech.gov.gds.serializer.CustomEmployeeSalaryDeSerializer;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = CustomEmployeeSalaryDeSerializer.class)
@JsonSerialize
public class EmployeeSalaryDTO {

    @JsonIgnore
    private Integer transactionId;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "salary")
    private BigDecimal salary;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private LocalDateTime createdTime;

    @JsonIgnore
    private String updatedBy;

    @JsonIgnore
    private LocalDateTime updatedTime;

    public static EmployeeSalaryDTO from(EmployeeSalary user) {
        if (user == null) return null;

        return EmployeeSalaryDTO.builder()
                .transactionId(user.getTransactionId())
                .name(user.getName())
                .salary(user.getSalary())
                .createdBy(user.getCreatedBy())
                .createdTime(user.getCreatedTime())
                .updatedBy(user.getUpdatedBy())
                .updatedTime(user.getUpdatedTime())
                .build();
    }

    public static EmployeeSalary to(EmployeeSalaryDTO user) {
        if (user == null) return null;

        return EmployeeSalary.builder()
                .transactionId(user.getTransactionId())
                .name(user.getName())
                .salary(user.getSalary())
                .createdBy(user.getCreatedBy())
                .createdTime(user.getCreatedTime())
                .updatedBy(user.getUpdatedBy())
                .updatedTime(user.getUpdatedTime())
                .build();
    }

    public static EmployeeSalaryDTO clone(EmployeeSalaryDTO user) {
        if (user == null) return null;

        return EmployeeSalaryDTO.builder()
                .transactionId(user.getTransactionId())
                .name(user.getName())
                .salary(user.getSalary())
                .createdBy(user.getCreatedBy())
                .createdTime(user.getCreatedTime())
                .updatedBy(user.getUpdatedBy())
                .updatedTime(user.getUpdatedTime())
                .build();
    }

}
