package com.tech.gov.gds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.gov.gds.model.UsersApiResponse;
import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;
import com.tech.gov.gds.service.EmployeeSalaryApiService;
import com.tech.gov.gds.util.Constants;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeSalaryController.class)
@CommonsLog
@Tag("unit")
@ActiveProfiles("test")
class EmployeeSalaryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeSalaryApiService employeeSalaryApiService;

    private EmployeeSalaryDTO user1, user2, user3;

    private UsersApiResponse usersApiResponseAll, usersApiResponse;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        user1 = EmployeeSalaryDTO.builder()
                .transactionId(1)
                .name("Martin Guptill")
                .salary(BigDecimal.valueOf(4000.00))
                .createdTime(LocalDateTime.now())
                .createdBy("ADMIN")
                .build();

        user2 = EmployeeSalaryDTO.builder()
                .transactionId(2)
                .name("Ish Sodhi")
                .salary(BigDecimal.valueOf(3275.5000000000))
                .createdTime(LocalDateTime.now())
                .createdBy("ADMIN")
                .build();

        user3 = EmployeeSalaryDTO.builder()
                .transactionId(2)
                .name("A Saai")
                .salary(BigDecimal.valueOf(6000.000))
                .createdTime(LocalDateTime.now())
                .createdBy("ADMIN")
                .build();


        usersApiResponseAll = UsersApiResponse.builder()
                .results(Arrays.asList(user3, user1, user2))
                .build();

        usersApiResponse = UsersApiResponse.builder()
                .results(Arrays.asList(user1, user2))
                .build();

    }

    @AfterEach
    void tearDown() {
        Mockito.clearInvocations(employeeSalaryApiService);
        Mockito.reset(employeeSalaryApiService);
    }

    @Nested
    class getEmployeeSalaryDetails {

        @Test
        void withoutAnyParam() throws Exception {
            when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), any())).thenReturn(usersApiResponseAll);

            String jsonExpected = "{\"results\":[{\"name\":\"A Saai\",\"salary\":6000.0000000000},{\"name\":\"Ish Sodhi\",\"salary\":3275.5000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000}]}";

            mvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(eq(BigDecimal.valueOf(0.0)),
                    eq(BigDecimal.valueOf(4000.0)),
                    eq(0),
                    eq(0),
                    eq(""));


        }

        @Test
        void withAllParam() throws Exception {
            when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), any())).thenReturn(usersApiResponse);

            String jsonExpected = "{\"results\":[{\"name\":\"Martin Guptill\",\"salary\":4000.0},{\"name\":\"Ish Sodhi\",\"salary\":3275.5}]}";

            mvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("min", "2000.0")
                    .param("max", "5000.0")
                    .param("offset", "10")
                    .param("limit", "100")
                    .param("sort", "name")
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(eq(BigDecimal.valueOf(2000.0000000000)),
                    eq(BigDecimal.valueOf(5000.0000000000)),
                    eq(10),
                    eq(100),
                    eq("name"));
        }

        @Test
        void withMinParam() throws Exception {
            when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), any())).thenReturn(usersApiResponse);

            String jsonExpected = "{\"results\":[{\"name\":\"Ish Sodhi\",\"salary\":3275.5000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000}]}";

            mvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("min", "2000.0")
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(eq(BigDecimal.valueOf(2000.0000000000)),
                    eq(BigDecimal.valueOf(4000.0000000000)),
                    eq(0),
                    eq(0),
                    eq(""));
        }

        @Test
        void withMaxParam() throws Exception {
            when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), any())).thenReturn(usersApiResponse);

            String jsonExpected = "{\"results\":[{\"name\":\"Ish Sodhi\",\"salary\":3275.5000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000}]}";

            mvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("max", "5000.0")
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(eq(BigDecimal.valueOf(0.0)),
                    eq(BigDecimal.valueOf(5000.0000000000)),
                    eq(0),
                    eq(0),
                    eq(""));
        }

        @Test
        void withSoryByParam() throws Exception {
            when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), eq("name"))).thenReturn(usersApiResponse);

            String jsonExpected = "{\"results\":[{\"name\":\"Ish Sodhi\",\"salary\":3275.5000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000}]}";

            mvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("sort", "name")
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(eq(BigDecimal.valueOf(0.0)),
                    eq(BigDecimal.valueOf(4000.0)),
                    eq(0),
                    eq(0),
                    eq("name"));
        }

        @Test
        void withSoryBySalaryIgnoreCaseParam() throws Exception {
            when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), eq("salary"))).thenReturn(usersApiResponse);

            String jsonExpected = "{\"results\":[{\"name\":\"Ish Sodhi\",\"salary\":3275.5000000000},{\"name\":\"Martin Guptill\",\"salary\":4000.0000000000}]}";

            mvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("sort", "Salary")
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
            verify(employeeSalaryApiService, times(1)).getEmployeeSalaryDetails(eq(BigDecimal.valueOf(0.0)),
                    eq(BigDecimal.valueOf(4000.0)),
                    eq(0),
                    eq(0),
                    eq("salary"));
        }

        @Test
        void withBad_SoryByParamValue() throws Exception {
            when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), any())).thenReturn(usersApiResponse);

            String jsonExpected = "{\"error\":\"Invalid parameter value found, allowed value is NULL or NAME or SALARY, but received value is SORT_BY_IS_WRONG_NAME\"}";

            mvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("sort", "SORT_BY_IS_WRONG_NAME")
            )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

            verify(employeeSalaryApiService, times(0)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
            verify(employeeSalaryApiService, times(0)).getEmployeeSalaryDetails(eq(BigDecimal.ZERO),
                    eq(BigDecimal.valueOf(4000.0)),
                    eq(0),
                    eq(0),
                    eq(""));
        }

        @Test
        void withBad_ParamValue() throws Exception {
            when(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), any())).thenReturn(usersApiResponse);

            String jsonExpected = "{\"error\":\"Parameter 'min' should be of type 'java.math.BigDecimal'\"}";

            mvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("min", "invalid number")
            )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(jsonExpected));

            verify(employeeSalaryApiService, times(0)).getEmployeeSalaryDetails(any(), any(), any(), any(), any());
            verify(employeeSalaryApiService, times(0)).getEmployeeSalaryDetails(eq(BigDecimal.ZERO),
                    eq(BigDecimal.valueOf(4000.0)),
                    eq(0),
                    eq(0),
                    eq(""));
        }
    }

    @Nested
    class form_upload {

        @Test
        void success() throws Exception {

            when(employeeSalaryApiService.uploadForm(any())).thenReturn(Arrays.asList(user1, user2));


            String jsonExpected = "{\"success\":1}";
            String bodyContent = "file=name,salary\n" +
                    "Martin Guptill,4000\n" +
                    "Ish Sodhi,3275.500";

            mvc.perform(post(Constants.ENTRY_POINT_FORM_DATA)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(bodyContent)
            )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

        }

        @Test
        void emptyFileData() throws Exception {

            when(employeeSalaryApiService.uploadForm(any())).thenReturn(Arrays.asList(user1, user2));

            String bodyContent = "file=";

            String jsonExpected = "{\"success\":0}";

            mvc.perform(post(Constants.ENTRY_POINT_FORM_DATA)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(bodyContent)
            )
                    .andExpect(status().isNoContent())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

        }

        @Test
        void fileDataIgnored() throws Exception {

            when(employeeSalaryApiService.uploadForm(any())).thenReturn(new ArrayList<>());

            String bodyContent = "file=name,salary\n" +
                    "Tim Southee,-2500.05\n" +
                    "Jos Buttler,-asd4000\n" +
                    "Malan,-2500.5\n" +
                    "Moeen Ali,0\n" +
                    "Morgan,6000";

            String jsonExpected = "{\"success\":0}";

            mvc.perform(post(Constants.ENTRY_POINT_FORM_DATA)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(bodyContent)
            )
                    .andExpect(status().isNoContent())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

        }

        @Test
        void exception() throws Exception {
            when(employeeSalaryApiService.uploadForm(any())).thenThrow(new RuntimeException("Error while load data"));

            String jsonExpected = "{\"error\":\"Error while load data\"}";

            String bodyContent = "file=name,salary\n" +
                    "Martin Guptill,4000\n" +
                    "Ish Sodhi,3275.500";

            mvc.perform(post(Constants.ENTRY_POINT_FORM_DATA)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(bodyContent)
            )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));
        }

        @Test
        void exception_parsingError() throws Exception {
            when(employeeSalaryApiService.uploadForm(any())).thenThrow(new RuntimeException("Error parsing CSV line:3. [Jos Buttler, abce4000]"));

            String jsonExpected = "{\"error\":\"Error parsing CSV line:3. [Jos Buttler, abce4000]\"}";

            String bodyContent = "file=name,salary\n" +
                    "Tim Southee,2500.05\n" +
                    "Jos Buttler,abce4000\n" +
                    "Malan,2500.5\n" +
                    "Moeen Ali,4200";

            mvc.perform(post(Constants.ENTRY_POINT_FORM_DATA)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(bodyContent)
            )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));
        }
    }

    @Nested
    class file_upload {

        @Test
        void success() throws Exception {

            when(employeeSalaryApiService.uploadFile(any())).thenReturn(Arrays.asList(user1, user2));


            MockMultipartFile file = new MockMultipartFile("file",
                    "user_test_data_1.csv",
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    EmployeeSalaryControllerTest.class.getClassLoader().getResource("static/sample/user_test_data_1.csv").openStream());


            String jsonExpected = "{\"success\":1}";

            mvc.perform(multipart(Constants.ENTRY_POINT_FILE_DATA)
                    .file(file)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

        }

        @Test
        void fileDataIgnored() throws Exception {

            when(employeeSalaryApiService.uploadForm(any())).thenReturn(new ArrayList<>());

            MockMultipartFile file = new MockMultipartFile("file",
                    "user_test_data_2_ignored.csv",
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    EmployeeSalaryControllerTest.class.getClassLoader().getResource("static/sample/user_test_data_2_ignored.csv").openStream());

            String jsonExpected = "{\"success\":0}";
            mvc.perform(multipart(Constants.ENTRY_POINT_FILE_DATA)
                    .file(file)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isNoContent())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

        }

        @Test
        void emptyFile() throws Exception {

            when(employeeSalaryApiService.uploadFile(any())).thenReturn(Arrays.asList(user1, user2));

            MockMultipartFile file = new MockMultipartFile("file",
                    "user_test_data_3_empty.csv",
                    MediaType.TEXT_PLAIN_VALUE,
                    "".getBytes(StandardCharsets.UTF_8)
            );

            String jsonExpected = "{\"success\":0}";

            mvc.perform(multipart(Constants.ENTRY_POINT_FILE_DATA)
                    .file(file)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isNoContent())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));

        }

        @Test
        void exception() throws Exception {
            when(employeeSalaryApiService.uploadFile(any())).thenThrow(new RuntimeException("Error while load data"));

            MockMultipartFile file = new MockMultipartFile("file",
                    "user_test_data_1.csv",
                    MediaType.TEXT_PLAIN_VALUE,
                    EmployeeSalaryControllerTest.class.getClassLoader().getResource("static/sample/user_test_data_1.csv").openStream());

            String jsonExpected = "{\"error\":\"Error while load data\"}";

            mvc.perform(multipart(Constants.ENTRY_POINT_FILE_DATA)
                    .file(file)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));
        }

        @Test
        void exception_parsingError() throws Exception {
            when(employeeSalaryApiService.uploadFile(any())).thenThrow(new RuntimeException("Error parsing CSV line:3. [Jos Buttler, abce4000]"));

            String jsonExpected = "{\"error\":\"Error parsing CSV line:3. [Jos Buttler, abce4000]\"}";

            MockMultipartFile file = new MockMultipartFile("file",
                    "user_test_data_3_parse_error.csv",
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    EmployeeSalaryControllerTest.class.getClassLoader().getResource("static/sample/user_test_data_3_parse_error.csv").openStream());

            mvc.perform(multipart(Constants.ENTRY_POINT_FILE_DATA)
                    .file(file)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonExpected));
        }
    }

}