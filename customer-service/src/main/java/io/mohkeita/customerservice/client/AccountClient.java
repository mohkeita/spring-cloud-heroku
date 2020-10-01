package io.mohkeita.customerservice.client;

import io.mohkeita.customerservice.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("account/customer/{customerId}")
    List<Account> findByCustomer(@PathVariable("customerId") String customerId);
}
