package com.igor.eduardo.currencyreactive.service.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.igor.eduardo.currencyreactive.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl {

    @Autowired
    private WireMockServer wireMockServer;
    public Flux<String> getUserName(final Mono<AccountModel> accountId) {
        return WebClient.create(wireMockServer.baseUrl())
                .get()
                .uri("/user")
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(String.class));
    }
}
