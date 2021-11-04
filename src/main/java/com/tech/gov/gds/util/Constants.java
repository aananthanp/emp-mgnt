package com.tech.gov.gds.util;

import java.math.BigDecimal;

public interface Constants {
    String API_BASE_URI = "/api";
    String ENTRY_POINT_USERS = "/users";
    String ENTRY_POINT_FILE_DATA ="/file_upload";
    String ENTRY_POINT_FORM_DATA ="/upload";
    String PARAM_MIN = "min";
    String PARAM_MAX = "max";
    String PARAM_OFFSET = "offset";
    String PARAM_LIMIT = "limit";
    String PARAM_SORT = "sort";
    BigDecimal DEFAULT_MIN_SALARY = BigDecimal.ZERO;
    BigDecimal DEFAULT_MAX_SALARY = BigDecimal.valueOf(4000.00);
    Integer DEFAULT_OFFSET = 0;
    Integer DEFAULT_LIMIT = 0;
    int RESPONSE_BUFFER_SIZE_5MB = 5 * 1024 *1024;
}
