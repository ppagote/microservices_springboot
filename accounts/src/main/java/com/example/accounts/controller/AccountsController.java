package com.example.accounts.controller;

import com.example.accounts.config.AccountsServiceConfig;
import com.example.accounts.model.*;
import com.example.accounts.repository.AccountRepository;
import com.example.accounts.service.client.CardsFeignClient;
import com.example.accounts.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountsController {

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
    @Autowired
    CardsFeignClient cardsFeignClient;
    @Autowired
    LoansFeignClient loansFeignClient;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountsServiceConfig accountsServiceConfig;

    @PostMapping("/myAccount")
    public Accounts getAccountDetails(@RequestBody Customer customer) {
        return accountRepository.findByCustomerId(customer.getCustomerId());
    }

    @GetMapping("/account/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                .withDefaultPrettyPrinter();

        Properties properties = new Properties(accountsServiceConfig.getMsg(), accountsServiceConfig.getBuildVersion(),
                accountsServiceConfig.getMailDetails(), accountsServiceConfig.getActiveBranches());
        return objectWriter.writeValueAsString(properties);
    }

    @PostMapping("/myCustomerDetails")
    //@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
    @Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
    public CustomerDetails myCustomerDetails(@RequestHeader("trace-id") String traceId, @RequestBody Customer customer) {
        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loansDetails = loansFeignClient.getLoansDetails(traceId, customer);
        List<Cards> cardsDetails = cardsFeignClient.getCardsDetails(traceId, customer);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setCards(cardsDetails);
        customerDetails.setLoans(loansDetails);
        return customerDetails;
    }

    private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("trace-id") String traceId, Customer customer, Throwable t) {
        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(traceId, customer);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        return customerDetails;
    }

    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
    public String sayHello() {
        return "Hello, Welcome to EazyBank Kubernetes cluster";
    }

    private String sayHelloFallback(Throwable t) {
        return "Hi, Welcome to EazyBank";
    }
}
