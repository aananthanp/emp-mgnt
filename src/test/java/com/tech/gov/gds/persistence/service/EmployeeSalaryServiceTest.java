package com.tech.gov.gds.persistence.service;

import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;
import com.tech.gov.gds.persistence.repo.EmployeeSalaryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@DataJpaTest
@Tag("unit")
@ActiveProfiles("test")
class EmployeeSalaryServiceTest {

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    private EmployeeSalaryService employeeSalaryService;

    private EmployeeSalaryDTO user1, user2, user3, user4, user5, user7, user8, user9, user10;

    private LocalDateTime createdTime, updatedTime;

    @BeforeEach
    void setUp() {
        this.employeeSalaryService = new EmployeeSalaryService(this.employeeSalaryRepository);
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now().plusMinutes(10);
        user1 = EmployeeSalaryDTO.builder()
                .transactionId(1)
                .name("Moeen Ali")
                .salary(new BigDecimal("2500.0500000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();

        user2 = EmployeeSalaryDTO.builder()
                .transactionId(2)
                .name("Mary Posa")
                .salary(new BigDecimal("4000.0000000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();

        user3 = EmployeeSalaryDTO.builder()
                .transactionId(3)
                .name("Harry")
                .salary(new BigDecimal("1500.5000000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();

        user4 = EmployeeSalaryDTO.builder()
                .transactionId(4)
                .name("Martin Guptill")
                .salary(new BigDecimal("4000.0000000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();


        user5  = EmployeeSalaryDTO.builder()
                .transactionId(5)
                .name("A Saai")
                .salary(new BigDecimal("6000.0000000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();

        user7 = EmployeeSalaryDTO.builder()
                .transactionId(7)
                .name("A Priya")
                .salary(new BigDecimal("6750.2000000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();

        user8 = EmployeeSalaryDTO.builder()
                .transactionId(8)
                .name("Ginny Weasley")
                .salary(new BigDecimal("3500.0000000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();
        user9 = EmployeeSalaryDTO.builder()
                .transactionId(9)
                .name("Ish Sodhi")
                .salary(new BigDecimal("3275.5000000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();
        user10= EmployeeSalaryDTO.builder()
                .transactionId(10)
                .name("Albus Dumbledore")
                .salary(new BigDecimal("4590.5000000000"))
                .createdTime(LocalDateTime.now())
                .createdBy("seed_data")
                .build();


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {

        user1.setUpdatedBy("ADMIN");
        user1.setUpdatedTime(updatedTime);
        EmployeeSalaryDTO user1_actual = this.employeeSalaryService.save(user1);
        this.employeeSalaryRepository.findByName(user1.getName()).ifPresent(u -> {
            user1.setTransactionId(u.getTransactionId());
            user1.setCreatedBy(u.getCreatedBy());
            user1.setCreatedTime(u.getCreatedTime());
        });
        assertThat(user1_actual).isEqualToIgnoringGivenFields(user1, "updatedTime");


        EmployeeSalaryDTO user2_actual = this.employeeSalaryService.save(user2);
        user2.setUpdatedBy("ADMIN");
        user2.setUpdatedTime(updatedTime);
        this.employeeSalaryRepository.findByName(user2.getName()).ifPresent(u -> {
            user2.setTransactionId(u.getTransactionId());
            user2.setCreatedBy(u.getCreatedBy());
            user2.setCreatedTime(u.getCreatedTime());
        });
        assertThat(user2_actual).isEqualToIgnoringGivenFields(user2, "updatedTime");


        EmployeeSalaryDTO user3_actual = this.employeeSalaryService.save(user3);
        user3.setUpdatedBy("ADMIN");
        user3.setUpdatedTime(updatedTime);
        this.employeeSalaryRepository.findByName(user3.getName()).ifPresent(u -> {
            user3.setTransactionId(u.getTransactionId());
            user3.setCreatedBy(u.getCreatedBy());
            user3.setCreatedTime(u.getCreatedTime());
        });
        assertThat(user3_actual).isEqualToIgnoringGivenFields(user3, "updatedTime");


        EmployeeSalaryDTO user4_actual = this.employeeSalaryService.save(user8);
        user8.setUpdatedBy("ADMIN");
        user8.setUpdatedTime(updatedTime);
        this.employeeSalaryRepository.findByName(user8.getName()).ifPresent(u -> {
            user8.setTransactionId(u.getTransactionId());
            user8.setCreatedBy(u.getCreatedBy());
            user8.setCreatedTime(u.getCreatedTime());
        });
        assertThat(user4_actual).isEqualToIgnoringGivenFields(user8, "updatedTime");
    }


    @Test
    void findById() {
        save();
        assertThat(employeeSalaryService.findById(user1.getTransactionId()).get()).isEqualToIgnoringGivenFields(user1, "updatedTime");;
        assertThat(employeeSalaryService.findById(user2.getTransactionId()).get()).isEqualToIgnoringGivenFields(user2, "updatedTime");;
        assertThat(employeeSalaryService.findById(-9999)).isEmpty();
    }

    @Test
    void isExistsByName() {
        save();
        assertTrue(employeeSalaryService.isExistsByName(user1.getName()));
        assertTrue(employeeSalaryService.isExistsByName(user2.getName()));
        assertFalse(employeeSalaryService.isExistsByName("wrong"));
    }

    @Test
    void delete() {
        save();
        assertThat(employeeSalaryService.findById(user1.getTransactionId()).get()).isEqualToIgnoringGivenFields(user1, "updatedTime");
        assertThat(employeeSalaryService.findById(user2.getTransactionId()).get()).isEqualToIgnoringGivenFields(user2, "updatedTime");

        employeeSalaryService.delete(user1.getTransactionId());
        assertThat(employeeSalaryService.findById(user1.getTransactionId())).isEmpty();

        employeeSalaryService.delete(user2.getTransactionId());
        assertThat(employeeSalaryService.findById(user2.getTransactionId())).isEmpty();

    }

    @Test
    void findByFilters_ByName() {
        List<EmployeeSalaryDTO> actual = this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 0, 2, "NAME");
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user7, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user5, "createdTime", "updatedTime");

        actual = this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 1, 2, "NAME");
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user8, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user3, "createdTime", "updatedTime");

        actual =this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 1, 0, "NAME");
        assertThat(actual.size()).isEqualTo(9);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user7, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user5, "createdTime", "updatedTime");
        assertThat(actual.get(2)).isEqualToIgnoringGivenFields(user8, "createdTime", "updatedTime");
        assertThat(actual.get(3)).isEqualToIgnoringGivenFields(user3, "createdTime", "updatedTime");
    }

    @Test
    void findByFilters_BySalary() {
        List<EmployeeSalaryDTO> actual = this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 0, 2, "SALARY");
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user3, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user1, "createdTime", "updatedTime");

        actual = this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 1, 2, "SALARY");
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user9, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user8, "createdTime", "updatedTime");

        actual =this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 1, 0, "SALARY");
        assertThat(actual.size()).isEqualTo(9);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user3, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user1, "createdTime", "updatedTime");
        assertThat(actual.get(2)).isEqualToIgnoringGivenFields(user9, "createdTime", "updatedTime");
        assertThat(actual.get(3)).isEqualToIgnoringGivenFields(user8, "createdTime", "updatedTime");
    }
    @Test
    void findByFilters_NoSorting() {
        List<EmployeeSalaryDTO> actual = this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 0, 2, "");
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user1, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user2, "createdTime", "updatedTime");

        actual = this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 1, 2, "");
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user3, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user4, "createdTime", "updatedTime");

        actual =this.employeeSalaryService.findByFilters(BigDecimal.ZERO, BigDecimal.valueOf(7000), 1, 0, "");
        assertThat(actual.size()).isEqualTo(9);
        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user1, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user2, "createdTime", "updatedTime");
        assertThat(actual.get(2)).isEqualToIgnoringGivenFields(user3, "createdTime", "updatedTime");
        assertThat(actual.get(3)).isEqualToIgnoringGivenFields(user4, "createdTime", "updatedTime");
        assertThat(actual.get(8)).isEqualToIgnoringGivenFields(user9, "createdTime", "updatedTime");
    }


    @Test
    void findByFilters_NoRecords() {
        List<EmployeeSalaryDTO> actual = this.employeeSalaryService.findByFilters(BigDecimal.valueOf(17000), BigDecimal.valueOf(27000), 0, 2, "");
        assertThat(actual).isEmpty();
    }

    @Order(1000)
    @Test
    void saveAll() {
        user1.setSalary(user1.getSalary().add(BigDecimal.TEN));
        user1.setUpdatedBy("ADMIN");

        user2.setSalary(user2.getSalary().add(BigDecimal.TEN));
        user2.setUpdatedBy("ADMIN");

        user3.setSalary(user3.getSalary().subtract(BigDecimal.TEN));
        user3.setUpdatedBy("ADMIN");

        user4.setCreatedBy(null);
        user4.setCreatedTime(null);
        user4.setUpdatedBy("ADMIN");

        List<EmployeeSalaryDTO> actual = this.employeeSalaryService.saveAll(Arrays.asList(user1, user2, user3, user4));

        user4.setCreatedBy("ADMIN");
        user4.setCreatedTime(LocalDateTime.now());
        assertThat(actual.size()).isEqualTo(4);

        assertThat(actual.get(0)).isEqualToIgnoringGivenFields(user1, "createdTime", "updatedTime");
        assertThat(actual.get(1)).isEqualToIgnoringGivenFields(user2, "createdTime", "updatedTime");
        assertThat(actual.get(2)).isEqualToIgnoringGivenFields(user3, "createdTime", "updatedTime");
        assertThat(actual.get(3)).isEqualToIgnoringGivenFields(user4, "createdTime", "updatedTime");
    }
}