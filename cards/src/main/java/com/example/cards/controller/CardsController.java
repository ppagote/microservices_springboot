package com.example.cards.controller;

import com.example.cards.config.CardsServiceConfig;
import com.example.cards.model.Cards;
import com.example.cards.model.Customer;
import com.example.cards.model.Properties;
import com.example.cards.repository.CardsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CardsController {

    private static final Logger logger = LoggerFactory.getLogger(CardsController.class);

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private CardsServiceConfig cardsServiceConfig;

    @PostMapping("/myCards")
    @Timed(value = "getCardDetails.time", description = "Time taken to return card details")
    public List<Cards> getCardDetails(@RequestHeader("trace-id") String traceId, @RequestBody Customer customer) {
        logger.info("getCardDetails method started");
        return cardsRepository.findByCustomerId(customer.getCustomerId());
    }

    @GetMapping("/card/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                .withDefaultPrettyPrinter();

        Properties properties = new Properties(cardsServiceConfig.getMsg(), cardsServiceConfig.getBuildVersion(),
                cardsServiceConfig.getMailDetails(), cardsServiceConfig.getActiveBranches());
        return objectWriter.writeValueAsString(properties);
    }

}