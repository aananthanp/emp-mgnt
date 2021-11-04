package com.tech.gov.gds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonSerialize
@JsonDeserialize
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class UsersApiResponse {
    @JsonProperty("results")
    private List<EmployeeSalaryDTO> results;
}
