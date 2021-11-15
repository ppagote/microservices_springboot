package com.example.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
@EnableEurekaClient
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/ppagote/accounts/**")
                        .filters(f -> f
                                .rewritePath("/ppagote/accounts/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString()))
                        .uri("lb://ACCOUNTS"))
                .route(p -> p
                        .path("/ppagote/loans/**")
                        .filters(f -> f
                                .rewritePath("/ppagote/loans/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString()))
                        .uri("lb://LOANS"))
                .route(p -> p
                        .path("/ppagote/cards/**")
                        .filters(f -> f
                                .rewritePath("/ppagote/cards/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString()))
                        .uri("lb://CARDS"))
                .build();
    }
}
