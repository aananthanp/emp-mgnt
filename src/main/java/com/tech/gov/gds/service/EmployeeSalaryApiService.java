package com.tech.gov.gds.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.tech.gov.gds.exception.RecordsCreationException;
import com.tech.gov.gds.model.UsersApiResponse;
import com.tech.gov.gds.model.dto.EmployeeSalaryDTO;
import com.tech.gov.gds.persistence.service.EmployeeSalaryService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@CommonsLog
public class EmployeeSalaryApiService {

    @Autowired
    private EmployeeSalaryService employeeSalaryService;
    private final int NO_OF_COLUMN =2;
    private final String COLUMN_SPLITER =",";
    /**
     * Upload the csv file
     *
     * @param file
     * @return
     * @throws IOException
     */
    public List<EmployeeSalaryDTO> uploadFile(MultipartFile file) throws IOException, CsvException {

        validateInputDataFormat(new InputStreamReader(file.getInputStream()));

        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        CsvToBean<EmployeeSalaryDTO> csvToBean = new CsvToBeanBuilder(reader)
                .withType(EmployeeSalaryDTO.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<EmployeeSalaryDTO> empSalaryList = csvToBean.parse();
        return SaveParsedRecords(empSalaryList);

    }

    private void validateInputDataFormat(Reader in) throws IOException, CsvException {
        CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
        try(CSVReader reader = new CSVReaderBuilder(
                in)
                .withCSVParser(csvParser)
                .build()){
            List<String[]> csvData = reader.readAll();
            csvData.forEach(line -> {
                if(line.length != 2) {
                    throw new RecordsCreationException(String.format("Invalid csv format received at line number %s, expected number of column is 2", line.length));
                }
            });
        }
    }
    /**
     * Upload the form data as csv content
     *
     * @param formData
     * @return
     * @throws IOException
     */
    public List<EmployeeSalaryDTO> uploadForm(StringBuffer formData) throws IOException, CsvException {

        validateInputDataFormat(new StringReader(formData.toString()));

        Reader reader = new BufferedReader(new StringReader(formData.toString()));

        CsvToBean<EmployeeSalaryDTO> csvToBean = new CsvToBeanBuilder(reader)
                .withType(EmployeeSalaryDTO.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<EmployeeSalaryDTO> empSalaryList = csvToBean.parse();
        return SaveParsedRecords(empSalaryList);

    }

    private List<EmployeeSalaryDTO> SaveParsedRecords(List<EmployeeSalaryDTO> empSalaryList) {
        List<EmployeeSalaryDTO> filteredSalaryList =  validateEmployeeSalaryData(empSalaryList);
        log.info(format("Employee Salary parsed, count=%s, after data validation count=%s", empSalaryList.size(), filteredSalaryList.size()));
        log.info(format("Employee Salary parsed -> %s", filteredSalaryList));

        if(filteredSalaryList.isEmpty())
            return filteredSalaryList;

        return this.saveAll(filteredSalaryList);
    }

    /**
     * Validate and discard the <=0 salary records
     *
     * @param empSalaryList
     * @return
     */
    protected List<EmployeeSalaryDTO> validateEmployeeSalaryData(List<EmployeeSalaryDTO> empSalaryList) {
        /*
         * Filter the < 0.0 values
         */
        return empSalaryList.stream()
                .filter(data -> data.getSalary().compareTo(BigDecimal.ZERO) >= 0)
                .collect(Collectors.toList());
    }

    /**
     * Get Employee Salary Details
     *
     * @param minSalary
     * @param maxSalary
     * @param offset
     * @param limit
     * @return
     */
    public UsersApiResponse getEmployeeSalaryDetails(BigDecimal minSalary,
                                                     BigDecimal maxSalary,
                                                     Integer offset,
                                                     Integer limit,
                                                     String sortBy) {

        return UsersApiResponse.builder()
                .results(this.employeeSalaryService.findByFilters(minSalary, maxSalary, offset, limit, sortBy))
                .build();
    }

    /***
     * Get Employee Salary by Id
     * @param transactionId
     * @return
     */
    public Optional<EmployeeSalaryDTO> getEmployeeSalary(Integer transactionId) {
        return this.employeeSalaryService.findById(transactionId);
    }

    /**
     * Check the records existing records and set the transaction no
     *
     * @param empSalaryList
     * @return
     */
    private List<EmployeeSalaryDTO> saveAll(List<EmployeeSalaryDTO> empSalaryList) {
        return this.employeeSalaryService.saveAll(empSalaryList.stream()
                .map(employeeSalaryDTO -> {
                    EmployeeSalaryDTO recordsToSave = EmployeeSalaryDTO.clone(employeeSalaryDTO);
                    this.employeeSalaryService.findByName(employeeSalaryDTO.getName())
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
                    return recordsToSave;
                }).collect(Collectors.toList()));
    }

    public boolean delete(Integer transactionId) {

        Optional<EmployeeSalaryDTO> existsUserById = this.employeeSalaryService.findById(transactionId);

        if (!existsUserById.isPresent()) {
            return false;
        }

        this.employeeSalaryService.delete(transactionId);

        return true;

    }
}
