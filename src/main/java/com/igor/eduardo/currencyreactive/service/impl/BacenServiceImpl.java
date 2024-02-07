package com.igor.eduardo.currencyreactive.service.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.igor.eduardo.currencyreactive.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class BacenServiceImpl {

    @Autowired
    private WireMockServer wireMockServer;


    public Mono<String> notify(final Mono<AccountModel> fromUser, final Mono<AccountModel> toAccount, final double toBeTransfered) {
        Random random = new Random();
        int i = random.nextInt(3);
        String uri;
        if(i==1){
            uri = "/bc";
        }
        else {
            uri = "/bc-error";
        }
        return WebClient.create(wireMockServer.baseUrl())
                .post()
                .uri(uri).header("fromUser", String.valueOf(fromUser.map(AccountModel::getId)))
                .header("toUser", String.valueOf(toAccount.map(AccountModel::getId)))
                .header("value", String.valueOf(toBeTransfered))
                .retrieve().onStatus(httpStatus -> httpStatus.value() == 429,
                        clientResponse -> Mono.empty()).bodyToMono(String.class);


    }
}
