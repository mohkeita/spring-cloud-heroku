package io.mohkeita.accountservice.repository;

import io.mohkeita.accountservice.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findByIdIn(List<String> ids);
    List<Account> findByCustomerId(String customerId);
}
