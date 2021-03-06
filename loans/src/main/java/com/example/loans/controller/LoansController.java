package com.example.loans.controller;

import com.example.loans.config.LoansServiceConfig;
import com.example.loans.model.Customer;
import com.example.loans.model.Loans;
import com.example.loans.model.Properties;
import com.example.loans.repository.LoansRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoansController {

    private static final Logger logger = LoggerFactory.getLogger(LoansController.class);

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private LoansServiceConfig accountsServiceConfig;

    @PostMapping("/myLoans")
    public List<Loans> getLoansDetails(@RequestHeader("trace-id") String traceId, @RequestBody Customer customer) {
        logger.info("Invoking Loans microservice");
        return loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
    }

    @GetMapping("/loan/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                .withDefaultPrettyPrinter();

        Properties properties = new Properties(accountsServiceConfig.getMsg(), accountsServiceConfig.getBuildVersion(),
                accountsServiceConfig.getMailDetails(), accountsServiceConfig.getActiveBranches());
        return objectWriter.writeValueAsString(properties);
    }

}