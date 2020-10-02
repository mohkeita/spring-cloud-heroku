package io.mohkeita.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mohkeita.orderservice.client.AccountClient;
import io.mohkeita.orderservice.client.CustomerClient;
import io.mohkeita.orderservice.client.ProductClient;
import io.mohkeita.orderservice.exception.ResourceNotFoundException;
import io.mohkeita.orderservice.model.*;
import io.mohkeita.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private OrderRepository repository;
    @Autowired
    private AccountClient accountClient;
    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private ProductClient productClient;

    @PostMapping
    public Order prepare(@RequestBody Order order) throws JsonProcessingException {
        int price = 0;
        List<Product> products = productClient.findByIds(order.getProductIds());
        LOGGER.info("Product found: {}", mapper.writeValueAsString(products));
        Customer customer = customerClient.findByIdWithAccounts(order.getCustomerId());
        LOGGER.info("Customer found: {}", mapper.writeValueAsString(customer));
        for (Product product : products)
                price += product.getPrice();
        final int priceDiscounted = priceDiscount(price, customer);
        LOGGER.info("Discounted price: {}", mapper.writeValueAsString(Collections.singletonMap("price", priceDiscounted)));
        Optional<Account> account = customer.getAccounts().stream().filter(a -> (a.getBalance() > priceDiscounted)).findFirst();
        if (account.isPresent()) {
            order.setAccountId(account.get().getId());
            order.setStatus(OrderStatus.ACCEPTED);
            order.setPrice(priceDiscounted);
            LOGGER.info("Account found: {}", mapper.writeValueAsString(account.get()));
        } else {
            order.setStatus(OrderStatus.REJECTED);
            LOGGER.info("Account not found: {}", mapper.writeValueAsString(customer.getAccounts()));
        }
        return repository.save(order);
    }

    @PutMapping("/{id}")
    public Order accept(@PathVariable String id) throws JsonProcessingException, ResourceNotFoundException {
        final Order order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + id));
        LOGGER.info("Order found: {}", mapper.writeValueAsString(order));
        accountClient.withdraw(order.getAccountId(), order.getPrice());
        HashMap<String, Object> log = new HashMap<>();
        log.put("accountid", order.getAccountId());
        log.put("price", order.getPrice());
        LOGGER.info("Account modified: {}", mapper.writeValueAsString(log));
        order.setStatus(OrderStatus.DONE);
        LOGGER.info("Order status changed: {}", mapper.writeValueAsString(Collections.singletonMap("status", order.getStatus())));
        repository.save(order);
        return order;
    }

    private int priceDiscount(int price, Customer customer) {
        double discount = 0;
        switch (customer.getType()) {
            case REGULAR:
                    discount += 0.05;
                    break;
            case VIP:
                    discount += 0.1;
                    break;

            default:
                    break;
        }
        int ordersNum = repository.countByCustomerId(customer.getId());
        discount += (ordersNum*0.01);
        return (int) (price - (price * discount));
    }
}
