package io.mohkeita.orderservice.repository;

import io.mohkeita.orderservice.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByIdIn(List<String> ids);
    int countByCustomerId(String customerId);
}
