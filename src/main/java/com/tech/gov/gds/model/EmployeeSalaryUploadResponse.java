package com.tech.gov.gds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

@Data
@JsonSerialize
@JsonDeserialize
@Builder
public class EmployeeSalaryUploadResponse {
    @JsonProperty("success")
    private Integer success;
}
