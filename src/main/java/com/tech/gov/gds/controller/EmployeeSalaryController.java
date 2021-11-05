package com.tech.gov.gds.controller;

import com.tech.gov.gds.model.ApiErrorResponse;
import com.tech.gov.gds.model.EmployeeSalaryUploadResponse;
import com.tech.gov.gds.model.UsersApiResponse;
import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;
import com.tech.gov.gds.model.dto.SortType;
import com.tech.gov.gds.service.EmployeeSalaryApiService;
import com.tech.gov.gds.util.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static com.tech.gov.gds.util.Constants.*;

@RestController
@CommonsLog
@Tag(name = "EmployeeSalary", description = "EmployeeSalary API")
public class EmployeeSalaryController {

    @Autowired
    private EmployeeSalaryApiService employeeSalaryApiService;

    @GetMapping(ENTRY_POINT_USERS)
    public ResponseEntity<Object> getUsers(
            @RequestParam(value = Constants.PARAM_MIN, required = false, defaultValue = DEFAULT_MIN_SALARY) BigDecimal recordMinSalary,
            @RequestParam(value = Constants.PARAM_MAX, required = false, defaultValue = DEFAULT_MAX_SALARY) BigDecimal recordMaxSalary,
            @RequestParam(value = Constants.PARAM_OFFSET, required = false, defaultValue = DEFAULT_OFFSET) Integer recordOffset,
            @RequestParam(value = Constants.PARAM_LIMIT, required = false, defaultValue = DEFAULT_LIMIT) Integer recordLimit,
            @RequestParam(value = Constants.PARAM_SORT, required = false, defaultValue = DEFAULT_SORT) String sort) {

        SortType recordSorting = SortType.NO_SORTING;
        if (StringUtils.hasText(sort)) {
            if (sort.equalsIgnoreCase(SortType.NAME.getSortType())) {
                recordSorting = SortType.NAME;
            } else if (sort.equalsIgnoreCase(SortType.SALARY.getSortType())) {
                recordSorting = SortType.SALARY;
            } else {
                String msg = String.format("Invalid parameter value found, allowed value is NULL or NAME or SALARY, but received value is %s", sort);
                log.info(msg);
                return ResponseEntity
                        .badRequest()
                        .body(ApiErrorResponse
                                .builder()
                                .error(msg)
                                .build());
            }
        }

        UsersApiResponse users = employeeSalaryApiService.getEmployeeSalaryDetails(recordMinSalary,
                recordMaxSalary,
                recordOffset,
                recordLimit,
                recordSorting.getSortType());

        return ResponseEntity.ok(users);
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST
     * application/x-www-form-urlencoded: the keys and values are encoded in key-value tuples separated by '&', with a '=' between the key and the value. Non-alphanumeric characters in both keys and values are percent encoded: this is the reason why this type is not suitable to use with binary data (use multipart/form-data instead)
     * multipart/form-data: each value is sent as a block of data ("body part"), with a user agent-defined delimiter ("boundary") separating each part. The keys are given in the Content-Disposition header of each part.
     *
     * @param file
     * @return
     */
    @PostMapping(path = ENTRY_POINT_FORM_DATA, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Object> formUpload(@RequestParam("file") StringBuffer file) {

        try {
            if (!StringUtils.hasText(file.toString())) {
                return new ResponseEntity<>(
                        EmployeeSalaryUploadResponse.builder()
                                .success(0)
                                .build(),
                        HttpStatus.NO_CONTENT);
            }

            List<EmployeeSalaryDTO> updatedRecords = this.employeeSalaryApiService.uploadForm(file);
            if (updatedRecords.isEmpty()) {
                return new ResponseEntity<>(
                        EmployeeSalaryUploadResponse.builder()
                                .success(0)
                                .build(),
                        HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(
                        EmployeeSalaryUploadResponse.builder()
                                .success(1)
                                .build(),
                        HttpStatus.OK);
            }

        } catch (Exception e) {
            log.error(String.format("Exception occurred while parse file, error=%s", e.getMessage()), e);
            return new ResponseEntity<>(
                    ApiErrorResponse.builder()
                            .error(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = ENTRY_POINT_FILE_DATA, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return new ResponseEntity<>(
                    EmployeeSalaryUploadResponse.builder()
                            .success(0)
                            .build(),
                    HttpStatus.NO_CONTENT);
        }

        try {
            List<EmployeeSalaryDTO> updatedRecords = this.employeeSalaryApiService.uploadFile(file);
            if (updatedRecords.isEmpty()) {
                return new ResponseEntity<>(
                        EmployeeSalaryUploadResponse.builder()
                                .success(0)
                                .build(),
                        HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(
                        EmployeeSalaryUploadResponse.builder()
                                .success(1)
                                .build(),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(String.format("Exception occurred while parse file, error=%s", e.getMessage()), e);
            return new ResponseEntity<>(
                    ApiErrorResponse.builder()
                            .error(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }
    }



}
