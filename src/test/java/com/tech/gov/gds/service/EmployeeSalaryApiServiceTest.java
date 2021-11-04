package com.tech.gov.gds.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.gov.gds.model.UsersApiResponse;
import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;
import com.tech.gov.gds.persistence.service.EmployeeSalaryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Tag("unit")
@ActiveProfiles("test")
@SpringBootTest(classes = EmployeeSalaryApiService.class)
class EmployeeSalaryApiServiceTest {

    @Autowired
    private EmployeeSalaryApiService employeeSalaryApiService;

    @MockBean
    private EmployeeSalaryService employeeSalaryService;

    private EmployeeSalaryDTO user1, user2, negativePrice1, negativePrice2, zeroPrice1;

    @BeforeEach
    void setUp() {

        user1 = EmployeeSalaryDTO.builder()
                .transactionId(1)
                .name("Martin Guptill")
                .salary(new BigDecimal("1234.00"))
                .createdTime(LocalDateTime.now())
                .createdBy("ADMIN")
                .build();

        user2 = EmployeeSalaryDTO.builder()
                .transactionId(2)
                .name("Daryl Mitchell")
                .salary(new BigDecimal("4434.50"))
                .createdTime(LocalDateTime.now())
                .createdBy("ADMIN")
                .build();

        negativePrice1 = EmployeeSalaryDTO.builder()
                .transactionId(5)
                .name("INVALID USER SALARY1")
                .salary(new BigDecimal("-1234.00"))
                .createdTime(LocalDateTime.now())
                .createdBy("ADMIN")
                .build();

        negativePrice2 = EmployeeSalaryDTO.builder()
                .transactionId(6)
                .name("INVALID USER SALARY 2")
                .salary(new BigDecimal("-234.50"))
                .createdTime(LocalDateTime.now())
                .createdBy("ADMIN")
                .build();
        zeroPrice1 = EmployeeSalaryDTO.builder()
                .transactionId(7)
                .name("VALID SALARY")
                .salary(new BigDecimal("0.0"))
                .createdTime(LocalDateTime.now())
                .createdBy("ADMIN")
                .build();


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getEmployeeSalaryDetails() throws IOException {

        when(this.employeeSalaryService.findByFilters(any(), any(), any(), any(), any()))
                .thenReturn(Arrays.asList(user1, user2, negativePrice1, negativePrice2));


        assertThat(employeeSalaryApiService.getEmployeeSalaryDetails(any(), any(), any(), any(), any()))
                .isEqualTo(UsersApiResponse.builder()
                        .results(Arrays.asList(user1, user2, negativePrice1, negativePrice2))
                        .build());

        Mockito.verify(employeeSalaryService, Mockito.times(1))
                .findByFilters(any(), any(), any(), any(), any());

    }


    @Test
    void getEmployeeSalary() throws IOException {

        when(this.employeeSalaryService.findById(eq(user1.getTransactionId())))
                .thenReturn(Optional.of(user1));


        assertThat(employeeSalaryApiService.getEmployeeSalary(user1.getTransactionId()))
                .isEqualTo(Optional.of(user1));

        Mockito.verify(employeeSalaryService, Mockito.times(1))
                .findById(any());

    }

    @Test
    void getEmployeeSalary_Invalid() throws IOException {

        when(this.employeeSalaryService.findById(any()))
                .thenReturn(Optional.empty());


        assertThat(employeeSalaryApiService.getEmployeeSalary(user1.getTransactionId()))
                .isEmpty();

        Mockito.verify(employeeSalaryService, Mockito.times(1))
                .findById(any());

    }

    @Test
    void delete() throws IOException {

        when(this.employeeSalaryService.findById(eq(user1.getTransactionId())))
                .thenReturn(Optional.of(user1));


        assertThat(employeeSalaryApiService.delete(user1.getTransactionId()))
                .isEqualTo(true);

        Mockito.verify(employeeSalaryService, Mockito.times(1))
                .delete(any());

    }

    @Test
    void delete_Invalid() throws IOException {

        when(this.employeeSalaryService.findById(any()))
                .thenReturn(Optional.empty());


        assertThat(employeeSalaryApiService.delete(user1.getTransactionId()))
                .isEqualTo(false);


        Mockito.verify(employeeSalaryService, Mockito.times(0))
                .delete(any());

    }

    @Test
    void upload() throws IOException {

        MockMultipartFile file = new MockMultipartFile("file",
                EmployeeSalaryApiServiceTest.class.getClassLoader()
                        .getResource("static/sample/user_test_data_1.csv").openStream());

        this.employeeSalaryApiService.uploadFile(file);

        Mockito.verify(employeeSalaryService, Mockito.times(1)).saveAll(any());

    }

    @Test
    void uploadFormData() throws IOException {

        StringBuffer formData = new StringBuffer();

        String user_test_data_1 = "name,salary\n" +
                "Tim Southee,2500.05\n" +
                "Jos Buttler,4000\n" +
                "Malan,2500.5\n" +
                "Moeen Ali,4200";

        formData.append(user_test_data_1);

        this.employeeSalaryApiService.uploadForm(formData);

        Mockito.verify(employeeSalaryService, Mockito.times(1)).saveAll(any());

    }

    @Test
    void uploadFormData_OnlyHeader() throws IOException {

        StringBuffer formData = new StringBuffer();

        String user_test_data_1 = "name,salary\n" ;

        formData.append(user_test_data_1);

        assertThat(this.employeeSalaryApiService.uploadForm(formData)).isEmpty();

        Mockito.verify(employeeSalaryService, Mockito.times(0)).saveAll(any());

    }

    @Test
    void uploadFormData_OnlyNegativePrice() throws IOException {

        StringBuffer formData = new StringBuffer();

        String user_test_data_1 = "name,salary\n" +
                "Tim Southee,-2500.05\n" +
                "Jos Buttler,-100000.00\n" +
                "Malan,-2500.5\n" +
                "Moeen Ali,-4200";

        formData.append(user_test_data_1);

        assertThat(this.employeeSalaryApiService.uploadForm(formData)).isEmpty();

        Mockito.verify(employeeSalaryService, Mockito.times(0)).saveAll(any());

    }

    @Test
    void uploadFormData_ParserError() throws IOException {

        StringBuffer formData = new StringBuffer();

        String user_test_data_1 = "name,salary\n" +
                "Tim Southee,2500.05\n" +
                "Jos Buttler,abce4000\n" +
                "Malan,2500.5\n" +
                "Moeen Ali,4200";

        formData.append(user_test_data_1);

        Exception exp = assertThrows(Exception.class, () -> {
            this.employeeSalaryApiService.uploadForm(formData);

        });
        Assertions.assertThat(exp.getMessage()).isEqualTo("Error parsing CSV line: 3. [Jos Buttler,abce4000]");
        Assertions.assertThat(exp.getClass()).isEqualTo(RuntimeException.class);

        Mockito.verify(employeeSalaryService, Mockito.times(0)).saveAll(any());

    }

    @Test
    void uploadFormData_validateEmployeeSalaryData() throws IOException {

        List<EmployeeSalaryDTO> actual = this.employeeSalaryApiService.validateEmployeeSalaryData(Arrays.asList(user1, user2, negativePrice1, negativePrice2, zeroPrice1));

        assertThat(actual).containsExactlyInAnyOrder(user1, user2, zeroPrice1);

    }

    @Test
    void uploadFormData_WithExistingRecords() throws IOException {

        StringBuffer formData = new StringBuffer();

        String user_test_data_1 = "name,salary\n" +
                "Tim Southee,2500.05\n" +
                "Martin Guptill,4000\n" +
                "Malan,2500.5\n" +
                "Daryl Mitchell,4200";

        formData.append(user_test_data_1);

        when(this.employeeSalaryService.findByName(eq("Martin Guptill"))).thenReturn(Optional.of(user1));
        when(this.employeeSalaryService.findByName(eq("Daryl Mitchell"))).thenReturn(Optional.of(user2));
        when(this.employeeSalaryService.findByName(eq("Tim Southee"))).thenReturn(Optional.empty());
        when(this.employeeSalaryService.findByName(eq("Malan"))).thenReturn(Optional.empty());
        when(this.employeeSalaryService.saveAll(any())).thenReturn(Arrays.asList(user1, user2));

        this.employeeSalaryApiService.uploadForm(formData);

        Mockito.verify(employeeSalaryService, Mockito.times(1)).saveAll(any());

    }

    @Test
    void test_JsonData() throws JsonProcessingException {
        /*MathContext ctx = new MathContext(10, RoundingMode.HALF_UP);*/
        BigDecimal t = new BigDecimal("1.050");
        t.setScale(10, RoundingMode.HALF_UP);

        /*BigDecimal t1 = new BigDecimal("1.050", ctx).divide(BigDecimal.ONE, 10, RoundingMode.HALF_UP);*/
    }


}