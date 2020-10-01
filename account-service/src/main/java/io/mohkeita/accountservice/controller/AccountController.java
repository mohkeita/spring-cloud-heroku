package io.mohkeita.accountservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mohkeita.accountservice.exception.ResourceNotFoundException;
import io.mohkeita.accountservice.model.Account;
import io.mohkeita.accountservice.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AccountRepository repository;

    @PostMapping
    public Account add(@RequestBody Account account) {
        return repository.save(account);
    }

    @PutMapping
    public Account update(@RequestBody Account account) {
        return repository.save(account);
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public Account withdraw(@PathVariable("id") String id, @PathVariable("amount") int amount) throws ResourceNotFoundException, JsonProcessingException {
        Account account = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for this id :: " + id));
        LOGGER.info("Account found: {}", mapper.writeValueAsString(account));
        account.setBalance(account.getBalance() - amount);
        LOGGER.info("Current balance: {}", mapper.writeValueAsString(Collections.singletonMap("balance", account.getBalance())));
        return repository.save(account);
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable("id") String id) throws ResourceNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for this id :: " + id));
    }

    @GetMapping("/customer/{customerId}")
    public List<Account> findByCustomerId(@PathVariable("customerId") String customerId) throws JsonProcessingException {
        List<Account> accounts = repository.findByCustomerId(customerId);
        LOGGER.info("Accounts found: customerId={}, accounts={}", customerId, mapper.writeValueAsString(accounts));
        return accounts;
    }

    @PostMapping("/ids")
    public List<Account> find(@RequestBody List<String> ids) {
        return repository.findByIdIn(ids);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) throws ResourceNotFoundException {
        Account account = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for this id :: " + id));

        repository.delete(account);
    }
}