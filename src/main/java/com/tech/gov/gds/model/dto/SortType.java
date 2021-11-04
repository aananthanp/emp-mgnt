package com.tech.gov.gds.model.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum SortType {

    NAME("name"),
    SALARY("salary"),
    NO_SORTING("");

    @Getter
    @JsonValue
    private String sortType;

    SortType(String sortType) {
        this.sortType = sortType;
    }
}
