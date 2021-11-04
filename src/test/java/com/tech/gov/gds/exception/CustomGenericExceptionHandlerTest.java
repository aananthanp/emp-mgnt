package com.tech.gov.gds.exception;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CustomGenericExceptionHandler.class})
@CommonsLog
@Tag("unit")
@ActiveProfiles("test")
class CustomGenericExceptionHandlerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void whenHttpRequestUnknownUri() throws Exception {
        mvc.perform(get("/unknow")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().is(404))
                .andExpect(result -> {
                    log.debug(result.getResponse().getContentAsString());
                })
                .andReturn();
    }
}