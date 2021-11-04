package com.tech.gov.gds.exception;

import com.tech.gov.gds.model.ApiErrorResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static java.lang.String.format;

@RestController()
@CommonsLog
public class CustomGenericExceptionHandler implements ErrorController {
    private static String CUSTOMIZE_ERROR_MESSAGE = "Error occurred, please contact our support engineers. ErrorCode=%s and Reference error id=%s";

    @RequestMapping("/error")
    public ResponseEntity<ApiErrorResponse> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String ticket = UUID.randomUUID().toString();
        Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String errorMessage = "Unknown error message";
        try {

            Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            if (throwable != null) {
                log.info(format("Error ticket : %s, HttpStatus=%s, error=%s, %s %s", ticket, status.toString(), throwable.getMessage(), request.getRequestURI()), throwable);
            } else {
                log.info(format("Error ticket : %s, HttpStatus=%s, error=%s, %s %s", ticket, status.toString(), "Unknown error message", request.getRequestURI(), request.getQueryString()));
            }

            if (status != null) {
                statusCode = Integer.valueOf(status.toString());
                if (statusCode == HttpStatus.NOT_FOUND.value()) {
                    errorMessage = String.format(CUSTOMIZE_ERROR_MESSAGE, "404", ticket);
                } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    errorMessage = String.format(CUSTOMIZE_ERROR_MESSAGE, "500", ticket);
                } else {
                    errorMessage = String.format(CUSTOMIZE_ERROR_MESSAGE, statusCode, ticket);
                }
            }
        } catch (Exception e) {
            log.info(String.format("Exception occurred", e.getMessage()), e);
        }

        return ResponseEntity
                .status(statusCode)
                .body(ApiErrorResponse
                        .builder()
                        .error(errorMessage)
                        .build());
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
