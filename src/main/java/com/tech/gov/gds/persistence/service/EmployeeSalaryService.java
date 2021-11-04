package com.tech.gov.gds.persistence.service;

import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;
import com.tech.gov.gds.model.dto.SortType;
import com.tech.gov.gds.persistence.entity.EmployeeSalary;
import com.tech.gov.gds.persistence.repo.EmployeeSalaryRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CommonsLog
public class EmployeeSalaryService {

    private final EmployeeSalaryRepository employeeSalaryRepository;

    public EmployeeSalaryService(EmployeeSalaryRepository employeeSalaryRepository) {
        this.employeeSalaryRepository = employeeSalaryRepository;
    }

    /**
     * Save records
     *
     * @param employeeSalaryDTO
     * @return
     */
    public EmployeeSalaryDTO save(EmployeeSalaryDTO employeeSalaryDTO) {
        EmployeeSalaryDTO recordsToSave = EmployeeSalaryDTO.clone(employeeSalaryDTO);
        this.findByName(employeeSalaryDTO.getName())
                .ifPresent(existsRecord -> {
                    /*
                     * User Name exists update the existing records
                     */
                    recordsToSave.setTransactionId(existsRecord.getTransactionId());
                    recordsToSave.setCreatedTime(existsRecord.getCreatedTime());
                    recordsToSave.setCreatedBy(existsRecord.getCreatedBy());
                    recordsToSave.setUpdatedBy("ADMIN");
                    recordsToSave.setUpdatedTime(LocalDateTime.now());

                    log.debug(String.format("Records found for %s and transaction id =%s, so update existing records",
                            employeeSalaryDTO.getName(), existsRecord.getTransactionId()));
                });
        return EmployeeSalaryDTO.from(this.employeeSalaryRepository.save(EmployeeSalaryDTO.to(recordsToSave)));
    }

    /**
     * find by Id
     *
     * @param transactionId
     * @return
     */
    public Optional<EmployeeSalaryDTO> findById(Integer transactionId) {
        return employeeSalaryRepository.findById(transactionId).map(EmployeeSalaryDTO::from);
    }

    /**
     * Check Name exists or not not
     *
     * @param name
     * @return
     */
    public boolean isExistsByName(String name) {
        return this.employeeSalaryRepository.existsByName(name);
    }

    /**
     * find by Name
     *
     * @param name
     * @return
     */
    public Optional<EmployeeSalaryDTO> findByName(String name) {
        return this.employeeSalaryRepository.findByName(name)
                .map(EmployeeSalaryDTO::from);
    }


    @Transactional
    public List<EmployeeSalaryDTO> saveAll(List<EmployeeSalaryDTO> salaryDTOS) {

        Iterable<EmployeeSalary> userIterable = () -> salaryDTOS
                .stream()
                .map(EmployeeSalaryDTO::to)
                .map(userDTO -> {
                    if (userDTO.getCreatedBy() == null) {
                        userDTO.setCreatedBy("ADMIN");
                    }

                    if (userDTO.getCreatedTime() == null) {
                        userDTO.setCreatedTime(LocalDateTime.now());
                    }

                    return userDTO;
                })
                .iterator();

        return this.employeeSalaryRepository.saveAll(userIterable)
                .stream()
                .map(EmployeeSalaryDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * Delete employee salary by Id
     *
     * @param id
     */
    public void delete(Integer id) {
        this.employeeSalaryRepository.deleteById(id);
    }

    /***
     * Find all the records bet ween min and max salary
     * @param minSalary
     * @param maxSalary
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    public List<EmployeeSalaryDTO> findByFilters(BigDecimal minSalary,
                                                 BigDecimal maxSalary,
                                                 Integer pageNo,
                                                 Integer pageSize,
                                                 String sortBy) {

        Sort sort = null;
        if (StringUtils.hasText(sortBy)) {
            sortBy = sortBy.toLowerCase();
            sort = Sort.by(Sort.Direction.ASC, sortBy);
        }

        if (pageSize < 1) {
            List<EmployeeSalary> employeeList = new ArrayList<>();
            if (StringUtils.hasText(sortBy)) {
                employeeList = this.employeeSalaryRepository.findBySalaryBetween(sort, minSalary, maxSalary);
            } else {
                employeeList = this.employeeSalaryRepository.findBySalaryBetween(minSalary, maxSalary);
            }
            return employeeList
                    .stream()
                    .map(EmployeeSalaryDTO::from)
                    .collect(Collectors.toList());
        }

        Pageable paging = StringUtils.hasText(sortBy) ? PageRequest.of(pageNo, pageSize, sort) : PageRequest.of(pageNo, pageSize);
        Page<EmployeeSalary> pagedResult = this.employeeSalaryRepository.findBySalaryBetween(paging, minSalary, maxSalary);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(EmployeeSalaryDTO::from)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
