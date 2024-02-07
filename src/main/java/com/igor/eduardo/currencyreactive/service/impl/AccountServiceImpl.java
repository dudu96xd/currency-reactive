package com.igor.eduardo.currencyreactive.service.impl;

import com.igor.eduardo.currencyreactive.model.AccountModel;
import com.igor.eduardo.currencyreactive.model.dto.TransferAccountDTO;
import com.igor.eduardo.currencyreactive.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BacenServiceImpl bcService;

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AccountServiceImpl(@Qualifier("kafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private Mono<AccountModel> subtract(final Mono<AccountModel> accountModel, final double value) {
        return this.accountRepository.findById(accountModel.map(AccountModel::getId))
                .map(p -> {
                    if (p.getCurrency() - value < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
                    }
                    p.setCurrency(p.getCurrency() - value);
                    return p;
                })
                .flatMap(p -> this.accountRepository.save(p));
    }

    private Mono<AccountModel> add(final Mono<AccountModel> accountModel, final double value) {
        return this.accountRepository.findById(accountModel.map(AccountModel::getId))
                .map(p -> {
                    p.setCurrency(p.getCurrency() + value);
                    return p;
                })
                .flatMap(p -> this.accountRepository.save(p));
    }

    @Transactional
    public Mono<AccountModel> transfer(final Mono<AccountModel> fromAccount, final Mono<AccountModel> toAccount, final double amount) {
        return subtract(fromAccount, amount).then(add(toAccount, amount));
    }


    public Mono<String> transferBetweenAccounts(TransferAccountDTO account) {
        Mono<AccountModel> fromUser = accountRepository.findById(account.getId()).map(accountModel -> {
                    userService.getUserName(Mono.just(accountModel));
                    return accountModel;
                }
        );
        Mono<AccountModel> toAccount = accountRepository.findById(account.getDestination()).map(accountModel -> {
                    userService.getUserName(Mono.just(accountModel));
                    return accountModel;
                }
        );
        return transfer(fromUser, toAccount, account.getToBeTranfered())
                .then(bcService.notify(fromUser, toAccount, account.getToBeTranfered()))
                .switchIfEmpty(Mono.just(""/*enviar msg para fila de contingencia*/));
    }
}
