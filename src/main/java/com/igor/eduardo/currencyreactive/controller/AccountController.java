package com.igor.eduardo.currencyreactive.controller;

import com.igor.eduardo.currencyreactive.model.AccountModel;
import com.igor.eduardo.currencyreactive.model.dto.TransferAccountDTO;
import com.igor.eduardo.currencyreactive.repository.AccountRepository;
import com.igor.eduardo.currencyreactive.service.impl.AccountServiceImpl;
import com.igor.eduardo.currencyreactive.service.impl.BacenServiceImpl;
import com.igor.eduardo.currencyreactive.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/account")
@RequiredArgsConstructor
class AccountController {

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl accountService;

    @Autowired
    public UserServiceImpl userService;

    @Autowired
    public BacenServiceImpl bcService;

    @GetMapping
    public Flux<AccountModel> all() {
        return this.accountRepository.findAll();
    }

    @PostMapping
    public Mono<AccountModel> create(@RequestBody AccountModel account) {
        return this.accountRepository.save(account);
    }

    @PostMapping("/transfer")
    public Mono<String> transfer(@RequestBody TransferAccountDTO account) {
        return accountService.transferBetweenAccounts(account);
    }

    @GetMapping("/{id}")
    public Mono<AccountModel> get(@PathVariable("id") Integer id) {
        return this.accountRepository.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<AccountModel> update(@PathVariable("id") Integer id, @RequestBody AccountModel account) {
        return this.accountRepository.findById(id)
                .map(p -> {
                    p.setCurrency(account.getCurrency());
                    return p;
                })
                .flatMap(p -> this.accountRepository.save(p));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") Integer id) {
        return this.accountRepository.deleteById(id);
    }

}