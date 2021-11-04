package com.tech.gov.gds.exception;

import com.tech.gov.gds.controller.EmployeeSalaryController;
import com.tech.gov.gds.model.UsersApiResponse;
import com.tech.gov.gds.service.EmployeeSalaryApiService;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {EmployeeSalaryController.class, CustomRestApiExceptionHandler.class})
@CommonsLog
@Tag("unit")
@ActiveProfiles("test")
class CustomRestApiExceptionHandlerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeSalaryApiService employeeSalaryApiService;

    @SpyBean
    private CustomRestApiExceptionHandler customRestApiExceptionHandler;

    @Test
    void withBad_ParamValue() throws Exception {
        when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), any())).thenReturn(UsersApiResponse.builder()
                .results(new ArrayList<>())
                .build());

        String jsonExpected = "{\"error\":\"Parameter 'min' should be of type 'java.math.BigDecimal'\"}";

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("min", "invalid number")
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonExpected));

        verify(customRestApiExceptionHandler, times(1)).handleMethodArgumentTypeMismatch(any(), any());

        verify(employeeSalaryApiService, times(0)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
        verify(employeeSalaryApiService, times(0)).getEmployeeSalaryDetails(eq(BigDecimal.ZERO),
                eq(BigDecimal.valueOf(4000.0)),
                eq(0),
                eq(0),
                eq(""));
    }
}