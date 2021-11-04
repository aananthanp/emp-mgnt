package com.tech.gov.gds.persistence.repo;

import com.tech.gov.gds.persistence.entity.EmployeeSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EmployeeSalaryRepository extends JpaRepository<EmployeeSalary, Integer> {

    boolean existsByName(String name);

    Optional<EmployeeSalary> findByName(String name);

    Page<EmployeeSalary> findBySalaryBetween(Pageable pageable, BigDecimal minSalary, BigDecimal maxSalary);

    List<EmployeeSalary> findBySalaryBetween(Sort sort, BigDecimal minSalary, BigDecimal maxSalary);

    List<EmployeeSalary> findBySalaryBetweenOrderByNameAsc(Sort sort, BigDecimal minSalary, BigDecimal maxSalary);
    List<EmployeeSalary> findBySalaryBetweenOrderBySalaryAsc(Sort sort, BigDecimal minSalary, BigDecimal maxSalary);

    Page<EmployeeSalary> findBySalaryBetweenOrderByNameAsc(Pageable pageable, BigDecimal minSalary, BigDecimal maxSalary);
    Page<EmployeeSalary> findBySalaryBetweenOrderBySalaryAsc(Pageable pageable, BigDecimal minSalary, BigDecimal maxSalary);

    List<EmployeeSalary> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);
}
