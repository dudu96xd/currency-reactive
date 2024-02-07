package com.igor.eduardo.currencyreactive.repository;

import com.igor.eduardo.currencyreactive.model.AccountModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccountRepository extends ReactiveCrudRepository<AccountModel, Integer> {
}