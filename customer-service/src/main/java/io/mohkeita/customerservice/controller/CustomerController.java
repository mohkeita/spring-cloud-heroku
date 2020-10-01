package io.mohkeita.customerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mohkeita.customerservice.client.AccountClient;
import io.mohkeita.customerservice.exception.ResourceNotFoundException;
import io.mohkeita.customerservice.model.Account;
import io.mohkeita.customerservice.model.Customer;
import io.mohkeita.customerservice.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private CustomerRepository repository;

    @PostMapping
    public Customer add(@RequestBody Customer customer) {
        return repository.save(customer);
    }

    @PutMapping
    public Customer update(@RequestBody Customer customer) {
        return repository.save(customer);
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable("id") String id) throws ResourceNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + id));
    }

    @GetMapping("/withAccounts/{id}")
    public Customer findByIdWithAccounts(@PathVariable("id") String id) throws JsonProcessingException, ResourceNotFoundException {
        List<Account> accounts = accountClient.findByCustomer(id);
        LOGGER.info("Accounts found: {}", mapper.writeValueAsString(accounts));
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + id));

        customer.setAccounts(accounts);
        return customer;
    }

    @PostMapping("/ids")
    public List<Customer> find(@RequestBody List<String> ids) {
        return repository.findByIdIn(ids);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) throws ResourceNotFoundException {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + id));

        repository.delete(customer);
    }
}
