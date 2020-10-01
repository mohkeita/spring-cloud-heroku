package io.mohkeita.customerservice.repository;

import io.mohkeita.customerservice.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    public List<Customer> findByIdIn(List<String> ids);
}
