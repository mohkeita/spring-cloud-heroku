package io.mohkeita.orderservice.client;

import io.mohkeita.orderservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/customer/withAccounts/{customerId}")
    Customer findByIdWithAccounts(@PathVariable("customerId") String customerId);
}
