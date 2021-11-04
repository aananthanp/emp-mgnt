package com.tech.gov.gds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.gov.gds.model.UsersApiResponse;
import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;
import com.tech.gov.gds.persistence.repo.EmployeeSalaryRepository;
import com.tech.gov.gds.persistence.service.EmployeeSalaryService;
import com.tech.gov.gds.testbase.AcceptanceTestBase;
import com.tech.gov.gds.util.Constants;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.tech.gov.gds.util.Constants.RESPONSE_BUFFER_SIZE_5MB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.*;

@CommonsLog
class EmployeeSalaryControllerAcceptanceTest extends AcceptanceTestBase {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    Environment environment;

    private WebClient webClient;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    private EmployeeSalaryDTO user1, user2, user3, user4, user5, user6, user7, user8, user9, user10;

    private String baseUrl = "http://localhost:8080";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        webClient = WebClient.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(RESPONSE_BUFFER_SIZE_5MB);
                })
                .baseUrl(String.format("http://localhost:%s", randomServerPort))
                .build();

        user1 = EmployeeSalaryDTO.builder()
                .transactionId(1)
                .name("Moeen Ali")
                .salary(new BigDecimal("2500.0500000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();

        user2 = EmployeeSalaryDTO.builder()
                .transactionId(2)
                .name("Mary Posa")
                .salary(new BigDecimal("4000.0000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();

        user3 = EmployeeSalaryDTO.builder()
                .transactionId(3)
                .name("Harry")
                .salary(new BigDecimal("1500.5000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();

        user4 = EmployeeSalaryDTO.builder()
                .transactionId(4)
                .name("Martin Guptill")
                .salary(new BigDecimal("4000.0000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();


        user5 = EmployeeSalaryDTO.builder()
                .transactionId(5)
                .name("A Saai")
                .salary(new BigDecimal("6000.0000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();

        user6 = EmployeeSalaryDTO.builder()
                .transactionId(6)
                .name("Severus Snape")
                .salary(new BigDecimal("4800.5000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();

        user7 = EmployeeSalaryDTO.builder()
                .transactionId(7)
                .name("A Priya")
                .salary(new BigDecimal("6750.2000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();

        user8 = EmployeeSalaryDTO.builder()
                .transactionId(8)
                .name("Ginny Weasley")
                .salary(new BigDecimal("3500.0000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();
        user9 = EmployeeSalaryDTO.builder()
                .transactionId(9)
                .name("Ish Sodhi")
                .salary(new BigDecimal("3275.5000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();
        user10 = EmployeeSalaryDTO.builder()
                .transactionId(10)
                .name("Albus Dumbledore")
                .salary(new BigDecimal("14590.5000000000"))
                .createdTime(LocalDateTime.parse("2021-11-01T09:10:40"))
                .createdBy("seed_data")
                .build();
    }

    @AfterEach
    void tearDown() {

    }


    @Test
    void test_acceptance_criteria_1_preloaded_data() throws JsonProcessingException {
        assertThat(employeeSalaryRepository.findAll().size()).isEqualTo(10);

        assertThat(employeeSalaryService.findById(user1.getTransactionId()).get()).isEqualToIgnoringGivenFields(user1, "updatedTime");
        assertThat(employeeSalaryService.findById(user2.getTransactionId()).get()).isEqualToIgnoringGivenFields(user2, "updatedTime");
        assertThat(employeeSalaryService.findById(user3.getTransactionId()).get()).isEqualToIgnoringGivenFields(user3, "updatedTime");
        assertThat(employeeSalaryService.findById(user4.getTransactionId()).get()).isEqualToIgnoringGivenFields(user4, "updatedTime");
        assertThat(employeeSalaryService.findById(user5.getTransactionId()).get()).isEqualToIgnoringGivenFields(user5, "updatedTime");
        assertThat(employeeSalaryService.findById(user6.getTransactionId()).get()).isEqualToIgnoringGivenFields(user6, "updatedTime");
        assertThat(employeeSalaryService.findById(user7.getTransactionId()).get()).isEqualToIgnoringGivenFields(user7, "updatedTime");
        assertThat(employeeSalaryService.findById(user8.getTransactionId()).get()).isEqualToIgnoringGivenFields(user8, "updatedTime");
        assertThat(employeeSalaryService.findById(user9.getTransactionId()).get()).isEqualToIgnoringGivenFields(user9, "updatedTime");
        assertThat(employeeSalaryService.findById(user10.getTransactionId()).get()).isEqualToIgnoringGivenFields(user10, "updatedTime");


        UsersApiResponse res = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_USERS)
                        .queryParam(Constants.PARAM_MIN, "0")
                        .queryParam(Constants.PARAM_MAX, "4000.00")
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return response.bodyToMono(UsersApiResponse.class);
                    } else if (response.statusCode()
                            .is4xxClientError()) {
                        return response.bodyToMono(UsersApiResponse.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
        assertThat(res).isNotNull();
        assertThat(res.getResults().size()).isEqualTo(6);
        assertThat(res.getResults().get(0)).isEqualToIgnoringGivenFields(user1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(res.getResults().get(1)).isEqualToIgnoringGivenFields(user2, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(res.getResults().get(2)).isEqualToIgnoringGivenFields(user3, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(res.getResults().get(3)).isEqualToIgnoringGivenFields(user4, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(res.getResults().get(4)).isEqualToIgnoringGivenFields(user8, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(res.getResults().get(5)).isEqualToIgnoringGivenFields(user9, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
    }

    @Test
    void test_acceptance_criteria_1_1() throws JsonProcessingException {
        String expected = "{\"results\":[{\"name\":\"Mary Posa\",\"salary\":4000.0000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000},{\"name\":\"A Saai\",\"salary\":6000.0000000000},{\"name\":\"Severus Snape\",\"salary\":4800.5000000000},{\"name\":\"A Priya\",\"salary\":6750.2000000000}]}";

        String res = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_USERS)
                        .queryParam(Constants.PARAM_MIN, "4000.00")
                        .queryParam(Constants.PARAM_MAX, "6800.00")
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode()
                            .is4xxClientError()) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
        log.info(res);

        assertThat(res).isEqualTo(expected);

        UsersApiResponse value = objectMapper.readValue(res, UsersApiResponse.class);

        assertThat(value).isNotNull();
        assertThat(value.getResults().size()).isEqualTo(5);
        assertThat(value.getResults().get(0)).isEqualToIgnoringGivenFields(user2, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(1)).isEqualToIgnoringGivenFields(user4, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(2)).isEqualToIgnoringGivenFields(user5, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(3)).isEqualToIgnoringGivenFields(user6, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(4)).isEqualToIgnoringGivenFields(user7, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");

    }

    @Test
    void test_acceptance_criteria_1_2_sort_by_name() throws JsonProcessingException {
        String expected = "{\"results\":[{\"name\":\"A Priya\",\"salary\":6750.2000000000},{\"name\":\"A Saai\",\"salary\":6000.0000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000},{\"name\":\"Mary Posa\",\"salary\":4000.0000000000},{\"name\":\"Severus Snape\",\"salary\":4800.5000000000}]}";

        String res = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_USERS)
                        .queryParam(Constants.PARAM_MIN, "4000.00")
                        .queryParam(Constants.PARAM_MAX, "6800.00")
                        .queryParam(Constants.PARAM_SORT, "NAME")
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode()
                            .is4xxClientError()) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
        log.info(res);

        assertThat(res).isEqualTo(expected);

        UsersApiResponse value = objectMapper.readValue(res, UsersApiResponse.class);


        assertThat(value).isNotNull();
        assertThat(value.getResults().size()).isEqualTo(5);
        assertThat(value.getResults().get(0)).isEqualToIgnoringGivenFields(user7, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(1)).isEqualToIgnoringGivenFields(user5, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(2)).isEqualToIgnoringGivenFields(user4, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(3)).isEqualToIgnoringGivenFields(user2, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(4)).isEqualToIgnoringGivenFields(user6, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
    }

    @Test
    void test_acceptance_criteria_1_2_sort_by_salary() throws JsonProcessingException {

        String expected = "{\"results\":[{\"name\":\"Mary Posa\",\"salary\":4000.0000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000},{\"name\":\"Severus Snape\",\"salary\":4800.5000000000},{\"name\":\"A Saai\",\"salary\":6000.0000000000},{\"name\":\"A Priya\",\"salary\":6750.2000000000}]}";

        String res = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_USERS)
                        .queryParam(Constants.PARAM_MIN, "4000.00")
                        .queryParam(Constants.PARAM_MAX, "6800.00")
                        .queryParam(Constants.PARAM_SORT, "SALARY")
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode()
                            .is4xxClientError()) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
        log.info(res);

        assertThat(res).isEqualTo(expected);

        UsersApiResponse value = objectMapper.readValue(res, UsersApiResponse.class);

        assertThat(value).isNotNull();
        assertThat(value.getResults().size()).isEqualTo(5);
        assertThat(value.getResults().get(0)).isEqualToIgnoringGivenFields(user2, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(1)).isEqualToIgnoringGivenFields(user4, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(2)).isEqualToIgnoringGivenFields(user6, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(3)).isEqualToIgnoringGivenFields(user5, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(4)).isEqualToIgnoringGivenFields(user7, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
    }

    @Test
    void test_acceptance_criteria_1_2_sort_by_Invalid_sory_key() throws JsonProcessingException {

        String expected = "{\"error\":\"Invalid parameter value found, allowed value is NULL or NAME or SALARY, but received value is invalid_sort_key\"}";

        String value = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_USERS)
                        .queryParam(Constants.PARAM_MIN, "4000.00")
                        .queryParam(Constants.PARAM_MAX, "6800.00")
                        .queryParam(Constants.PARAM_SORT, "invalid_sort_key")
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode()
                            .is4xxClientError()) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();


        assertThat(value).isNotNull();
        assertThat(value).isEqualTo(expected);
    }

    @Test
    void test_acceptance_criteria_1_3() throws JsonProcessingException {
        String expected = "{\"results\":[{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000},{\"name\":\"Ginny Weasley\",\"salary\":3500.0000000000},{\"name\":\"Ish Sodhi\",\"salary\":3275.5000000000}]}";

        String res = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_USERS)
                        .queryParam(Constants.PARAM_OFFSET, "1")
                        .queryParam(Constants.PARAM_LIMIT, "3")
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode()
                            .is4xxClientError()) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
        log.info(res);
        assertThat(res).isEqualTo(expected);

        UsersApiResponse value = objectMapper.readValue(res, UsersApiResponse.class);

        assertThat(value).isNotNull();
        assertThat(value.getResults().size()).isEqualTo(3);
        assertThat(value.getResults().get(0)).isEqualToIgnoringGivenFields(user4, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(1)).isEqualToIgnoringGivenFields(user8, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(value.getResults().get(2)).isEqualToIgnoringGivenFields(user9, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
    }

    @Test
    void test_acceptance_criteria_() throws JsonProcessingException {
        String value = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_USERS)
                        .queryParam(Constants.PARAM_MIN, "4000.00")
                        .queryParam(Constants.PARAM_MAX, "6800.00")
                        .build())
                .exchangeToMono(response -> {
                    if (response.statusCode()
                            .equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode()
                            .is4xxClientError()) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
                .block();
        log.info(value);
        UsersApiResponse response = objectMapper.readValue(value, UsersApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getResults().size()).isEqualTo(5);
    }

    @Test
    void test_JsonData() throws JsonProcessingException {
        String value = "{\"results\":[{\"name\":\"Moeen Ali\",\"salary\":2500.0500000000},{\"name\":\"Mary Posa\",\"salary\":4000.0000000000},{\"name\":\"Harry\",\"salary\":1500.5000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000},{\"name\":\"Ginny Weasley\",\"salary\":3500.0000000000},{\"name\":\"Ish Sodhi\",\"salary\":3275.5000000000}]}";
        UsersApiResponse response = objectMapper.readValue(value, UsersApiResponse.class);
        assertThat(response).isNotNull();
    }

    public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", new FileSystemResource(file));
        return builder.build();
    }

    @Test
    @Description("Upload with a properly structured CSV file. You may include any data in the csv file")
    void test_acceptance_criteria_2_any_data() throws IOException {
        EmployeeSalaryDTO result1 = EmployeeSalaryDTO.builder()
                .name("Kane Richardson")
                .salary(new BigDecimal(13500.76).setScale(10, RoundingMode.HALF_UP))
                .createdBy("ADMIN")
                .build();
        EmployeeSalaryDTO result2 = EmployeeSalaryDTO.builder()
                .name("Mitchell Swepson")
                .salary(new BigDecimal(12225.5).setScale(10, RoundingMode.HALF_UP))
                .createdBy("ADMIN")
                .build();

        String filePath = "classpath:static/sample/user_test_data_1_any.csv";
        List<String> lines = Collections.emptyList();
        lines = Files.readAllLines(Paths.get(resourceLoader.getResource(filePath).getURI()));
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("file", lines.stream().collect(Collectors.joining(System.lineSeparator())));


        String expected = "{\"success\":1}";

        String value = webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_FORM_DATA)
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info(value);
        assertThat(value).isEqualTo(expected);

        assertThat(employeeSalaryService.findByName(result1.getName()).get()).isEqualToIgnoringGivenFields(result1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(employeeSalaryService.findByName(result2.getName()).get()).isEqualToIgnoringGivenFields(result2, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
    }

    @Test
    @Description("File should include some new data that is not in the database, and some that overwrites the database")
    void test_acceptance_criteria_2_new_data_and_update() throws IOException {
        EmployeeSalaryDTO result1 = EmployeeSalaryDTO.builder()
                .name("Victoria Tim")
                .salary(new BigDecimal(12500.05).setScale(10, RoundingMode.HALF_UP))
                .createdBy("ADMIN")
                .build();
        EmployeeSalaryDTO result2 = EmployeeSalaryDTO.builder()
                .name("zara Shami")
                .salary(new BigDecimal(13275.5).setScale(10, RoundingMode.HALF_UP))
                .createdBy("ADMIN")
                .build();
        EmployeeSalaryDTO result3 = EmployeeSalaryDTO.builder()
                .name("Xin")
                .salary(new BigDecimal(10101.5).setScale(10, RoundingMode.HALF_UP))
                .createdBy("ADMIN")
                .build();

        String filePath = "classpath:static/sample/user_test_data_1_new_data.csv";
        List<String> lines = Collections.emptyList();
        lines = Files.readAllLines(Paths.get(resourceLoader.getResource(filePath).getURI()));
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("file", lines.stream().collect(Collectors.joining(System.lineSeparator())));


        String expected = "{\"success\":1}";

        String value = webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_FORM_DATA)
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info(value);
        assertThat(value).isEqualTo(expected);

        assertThat(employeeSalaryService.findByName(result1.getName()).get()).isEqualToIgnoringGivenFields(result1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(employeeSalaryService.findByName(result2.getName()).get()).isEqualToIgnoringGivenFields(result2, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");

        /*
         * Test for Update Operation
         */
        assertThat(employeeSalaryService.findByName(result3.getName())).isEmpty();

        filePath = "classpath:static/sample/user_test_data_1_update_data.csv";
        lines = Files.readAllLines(Paths.get(resourceLoader.getResource(filePath).getURI()));
        formData = new LinkedMultiValueMap<>();
        formData.add("file", lines.stream().collect(Collectors.joining(System.lineSeparator())));


        expected = "{\"success\":1}";

        value = webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_FORM_DATA)
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info(value);
        assertThat(value).isEqualTo(expected);

        result1.setSalary(new BigDecimal(11234.05).setScale(10, RoundingMode.HALF_UP));
        result1.setUpdatedBy("ADMIN");

        assertThat(employeeSalaryService.findByName(result1.getName()).get()).isEqualToIgnoringGivenFields(result1, "transactionId", "createdBy", "createdTime", "updatedTime");
        assertThat(employeeSalaryService.findByName(result3.getName()).get()).isEqualToIgnoringGivenFields(result3, "transactionId", "createdBy", "createdTime", "updatedTime");
    }

    /***
     * Test : File should include rows with negative and 0.00 salary.
     * /users should work as expected after the upload and that there are new results returned as well as previous results that have been overwritten.
     *   Negative rows should be ignored in the input and 0.0 should be updated and returned.
     * @throws IOException
     */
    @Test
    @Description("File should include rows with negative and 0.00 salary.")
    void test_acceptance_criteria_2_negative_zero_value() throws IOException {
        EmployeeSalaryDTO newRecords = EmployeeSalaryDTO.builder()
                .name("Xin II")
                .salary(new BigDecimal(11101.5).setScale(10, RoundingMode.HALF_UP))
                .createdBy("ADMIN")
                .build();
        EmployeeSalaryDTO zeroValue = EmployeeSalaryDTO.builder()
                .name("Moeen Ali II")
                .salary(new BigDecimal(0).setScale(10, RoundingMode.HALF_UP))
                .createdBy("ADMIN")
                .build();

        String filePath = "classpath:static/sample/user_test_data_2_negative_zero.csv";
        List<String> lines = Collections.emptyList();
        lines = Files.readAllLines(Paths.get(resourceLoader.getResource(filePath).getURI()));
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("file", lines.stream().collect(Collectors.joining(System.lineSeparator())));
        String expected = "{\"success\":1}";


        assertThat(employeeSalaryService.findByName(user1.getName()).get()).isEqualToIgnoringGivenFields(user1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
        assertThat(employeeSalaryService.findByName(newRecords.getName())).isEmpty();
        assertThat(employeeSalaryService.findByName(zeroValue.getName())).isEmpty();

        String value = webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_FORM_DATA)
                        .build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info(value);
        assertThat(value).isEqualTo(expected);

        assertThat(employeeSalaryService.findByName(newRecords.getName()).get()).isEqualToIgnoringGivenFields(newRecords, "transactionId", "createdBy", "createdTime", "updatedTime");
        assertThat(employeeSalaryService.findByName(zeroValue.getName()).get()).isEqualToIgnoringGivenFields(zeroValue, "transactionId", "createdBy", "createdTime", "updatedTime");

        /*
        * check /users
         */
        expected ="{\"results\":[{\"name\":\"Moeen Ali II\",\"salary\":0E-10}]}";
        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_USERS)
                        .queryParam(Constants.PARAM_MIN, "-10000.00")
                        .queryParam(Constants.PARAM_MAX, "100.00")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody().json(expected);
    }

    @Test
    void test_acceptance_criteria_2_all_negative_value_check_no_content_status() throws IOException {
        String filePath = "classpath:static/sample/user_test_data_2_ignored.csv";
        List<String> lines = Collections.emptyList();
        lines = Files.readAllLines(Paths.get(resourceLoader.getResource(filePath).getURI()));
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("file", lines.stream().collect(Collectors.joining(System.lineSeparator())));

        assertThat(employeeSalaryService.findByName(user1.getName()).get()).isEqualToIgnoringGivenFields(user1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");

        webTestClient
                .method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_FORM_DATA)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isNoContent()
                .expectBody().isEmpty()
        ;

        assertThat(employeeSalaryService.findByName(user1.getName()).get()).isEqualToIgnoringGivenFields(user1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
    }

    /**
     * Upload with an improperly structured CSV file that should contain at least some good rows.
     * File should be rejected and none of the good rows should have been applied.
     */
    @Test
    @Description("Upload with an improperly structured CSV file that should contain at least some good rows")
    void test_acceptance_criteria_3() throws IOException {
        String filePath = "classpath:static/sample/user_test_data_3_parse_error.csv";
        List<String> lines = Collections.emptyList();
        lines = Files.readAllLines(Paths.get(resourceLoader.getResource(filePath).getURI()));
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("file", lines.stream().collect(Collectors.joining(System.lineSeparator())));

        int currentCount = employeeSalaryRepository.findAll().size();
        assertThat(employeeSalaryService.findByName(user1.getName()).get()).isEqualToIgnoringGivenFields(user1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");

        webTestClient
                .method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_FORM_DATA)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().is4xxClientError()
                .expectBody().json("{\"error\":\"Error parsing CSV line: 3. [Jos Buttler,abce4000]\"}")
        ;
        assertThat(employeeSalaryRepository.findAll().size()).isEqualTo(currentCount);
        assertThat(employeeSalaryService.findByName("Tim Southee")).isEmpty();
        assertThat(employeeSalaryService.findByName("Malan")).isEmpty();
        assertThat(employeeSalaryService.findByName(user1.getName()).get()).isEqualToIgnoringGivenFields(user1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
    }

    private MultipartBodyBuilder getMultipartFile(String filePath) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("fieldPart", "fieldValue", MediaType.APPLICATION_FORM_URLENCODED);
        builder.part("file", new FileSystemResource(resourceLoader.getResource(filePath).getURI().getPath()), MediaType.APPLICATION_FORM_URLENCODED);
        return builder;
    }

    @Test
    void test_acceptance_criteria_3_in_correct_column() throws IOException {
        String filePath = "classpath:static/sample/user_test_data_3_no_of_column.csv";
        List<String> lines = Collections.emptyList();
        lines = Files.readAllLines(Paths.get(resourceLoader.getResource(filePath).getURI()));
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("file", lines.stream().collect(Collectors.joining(System.lineSeparator())));

        int currentCount = employeeSalaryRepository.findAll().size();
        assertThat(employeeSalaryService.findByName(user1.getName()).get()).isEqualToIgnoringGivenFields(user1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");

        webTestClient
                .method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder.path(Constants.ENTRY_POINT_FORM_DATA)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().is4xxClientError()
                .expectBody().json("{\"error\":\"Invalid csv format received at 1, expected number of column is 2\"}")
        ;
        assertThat(employeeSalaryRepository.findAll().size()).isEqualTo(currentCount);
        assertThat(employeeSalaryService.findByName("Tim Southee")).isEmpty();
        assertThat(employeeSalaryService.findByName("Malan")).isEmpty();
        assertThat(employeeSalaryService.findByName(user1.getName()).get()).isEqualToIgnoringGivenFields(user1, "transactionId", "createdBy", "createdTime", "updatedBy", "updatedTime");
    }

}
