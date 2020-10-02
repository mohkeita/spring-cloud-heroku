package io.mohkeita.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mohkeita.productservice.exception.ResourceNotFoundException;
import io.mohkeita.productservice.model.Product;
import io.mohkeita.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ProductRepository repository;

    @PostMapping
    public Product add(@RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping
    public Product update(@RequestBody Product product) {
        return repository.save(product);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable("id") String id) throws ResourceNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));
    }

    @PostMapping("/ids")
    public List<Product> find(@RequestBody List<String> ids) throws JsonProcessingException {
        List<Product> products = repository.findByIdIn(ids);
        LOGGER.info("Products found: {}", mapper.writeValueAsString(Collections.singletonMap("count", products.size())));
        return products;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) throws ResourceNotFoundException {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));

        repository.delete(product);
    }
}
