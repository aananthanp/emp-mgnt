package com.tech.gov.gds.util;

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

    String DEFAULT_MIN_SALARY = "0.0";
    String DEFAULT_MAX_SALARY = "4000.0";
    String DEFAULT_OFFSET = "0";
    String DEFAULT_LIMIT = "0";
    String DEFAULT_SORT = "";
    int RESPONSE_BUFFER_SIZE_5MB = 5 * 1024 *1024;
}
