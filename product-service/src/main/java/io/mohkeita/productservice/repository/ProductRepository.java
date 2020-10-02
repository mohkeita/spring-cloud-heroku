package io.mohkeita.productservice.repository;

import io.mohkeita.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByIdIn(List<String> ids);
}
