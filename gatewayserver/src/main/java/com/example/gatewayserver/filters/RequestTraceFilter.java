package com.example.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isTraceIdPresent(requestHeaders)) {
            logger.debug("EazyBank-trace-id found in tracing filter: {}. ",
                    filterUtility.getTraceId(requestHeaders));
        } else {
            String traceID = generateTraceId();
            exchange = filterUtility.setTraceId(exchange, traceID);
            logger.debug("EazyBank-trace-id generated in tracing filter: {}.", traceID);
        }
        return chain.filter(exchange);
    }

    private boolean isTraceIdPresent(HttpHeaders requestHeaders) {
        return filterUtility.getTraceId(requestHeaders) != null;
    }

    private String generateTraceId() {
        return java.util.UUID.randomUUID().toString();
    }

}