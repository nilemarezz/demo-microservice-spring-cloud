package com.example.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class CustomFilter implements GlobalFilter {
    Logger logger = LoggerFactory.getLogger(CustomFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // before request go to service
        ServerHttpRequest request = exchange.getRequest();
        logger.info("Authorization : " + request.getHeaders().getFirst("Authorization"));
        if(request.getURI().toString().contains("/api/student")){
            logger.info("Call To Student Service");
        }
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // after service process and send to user
            ServerHttpResponse response = exchange.getResponse();
            logger.info("Post Filter : " + response.getStatusCode());
        }));
    }
}
