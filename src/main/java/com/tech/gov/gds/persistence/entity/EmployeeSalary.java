package com.tech.gov.gds.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public", name = "Employee_Salary")
public class EmployeeSalary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_sequence")
    @SequenceGenerator(name = "transaction_id_sequence", sequenceName = "public.transaction_id_sequence", allocationSize = 1)
    private Integer transactionId;

    private String name;

    private BigDecimal salary;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;
}
